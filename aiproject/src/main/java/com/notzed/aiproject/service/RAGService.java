package com.notzed.aiproject.service;

import com.notzed.aiproject.dto.ChatDto;
import com.notzed.aiproject.tools.RouterTools;
import com.notzed.aiproject.tools.StockTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    private final StockTools stockTools;
    private final RouterTools routerTools;

    @Value("classpath:policy.pdf")
    Resource pdfFile;

    public String chatWithAI(ChatDto request, String userId){

        String message = request.getMessage();

        String systemPrompt =String.format("""
                You are a friendly chatting assistant.
                IMPORTANT: The current user's ID is "%s".
                """,userId);

        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .advisors(
                        new SafeGuardAdvisor(List.of("competitor")),

                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(userId)
                                .build()

                )
                .call()
                .content();
    }



    public String askAIForVibe(String prompt){

        String template = """
                You are a music recommendation assistant.
                
                STRICT RULES:
                - You ONLY know the songs listed in the Context section below.
                - You MUST recommend ONLY songs that appear in the Context.
                - NEVER mention any song outside of the Context.
                - NEVER mention any artist or band name — you don't know them.
                - The context has the song title — use EXACTLY that title.
                - If context is provided, ALWAYS recommend from it. Never say I don't know.
                
                Context:
                {context}
                
                Based ONLY on the context above, recommend the song in a warm friendly tone.
                Mention the exact song title and explain why it matches the feeling.
                """;

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                        .query(prompt)
                        .topK(3)
                        .similarityThreshold(0.3)
                        .build());

        if(documents.isEmpty()){
            return "Sorry, I couldn't find any matching songs for that feeling!";
        }

        String context = documents.stream()
                        .map(d -> "Song Title: " + d.getMetadata().get("title") +
                                "\nGenre: " + d.getMetadata().get("genre") +
                                "\nDescription: " + d.getText())
                        .collect(Collectors.joining("\n\n"));

        System.out.println("Final Context:\n" + context);

        PromptTemplate promptTemplate =new PromptTemplate(template);
        String systemPrompt = promptTemplate.render(Map.of("context", context));

        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .advisors()
                .call()
                .content();
    }

    public void ingestVectorStore(){
        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfFile);
        List<Document> pages = reader.get();

        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(200)
                .build();
        List<Document> chunks =tokenTextSplitter.apply(pages);

        vectorStore.add(chunks);
    }



    public String chatWithAIForStocks(String prompt){
        String systemPrompt = """
                You are an AI assistant helping a developer to buy stocks.
                
                Rules:
                - Use ONLY the information provided in the context
                - The output generated SHOULD be "I checked the price (e.g, it was 150)  and bought X (e.g, 10) shares for you."
                - Do NOT introduce new concepts or facts
                
                Answer in a friendly, conversational tone.
                """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .tools(stockTools)
                .call()
                .content();
    }




    public String askAIAboutEmployeeHandbook(String prompt){
        String template = """
                You are an AI assistant helping a developer.
                
                Rules:
                - Use ONLY the information provided in the context
                - You MAY rephrase, summarize, and explain in the natural language
                - Do NOT introduce new concepts or facts
                - If multiple context sections are relevant, combine them into a single explanation.
                - If the answer is not present, say "I don't know"
                
                Context:
                {context}
                
                Answer in a friendly, conversational tone.
                """;

        List<Document> documents =vectorStore.similaritySearch(SearchRequest.builder()
                        .query(prompt)
                        .topK(3)
                        .similarityThreshold(0.2)
                        .filterExpression("file_name == 'policy.pdf'")
                    .build());

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        PromptTemplate promptTemplate = new PromptTemplate(template);
        String systemPrompt = promptTemplate.render(Map.of("context", context));

        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .content();
    }

    public String askAIAboutManual(String prompt){
        String template = """
                You are an AI assistant helping a developer.
                
                Rules:
                - Use ONLY the information provided in the context
                - You MAY rephrase, summarize, and explain in the natural language
                - Do NOT introduce new concepts or facts
                - If multiple context sections are relevant, combine them into a single explanation.
                - If the answer is not present, say "I don't know"
                
                Context:
                {context}
                
                Answer in a friendly, conversational tone.
                """;

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                .query(prompt)
                .topK(3)
                .similarityThreshold(0.3)
                .filterExpression("file_name == 'Internet_Router_User_Manual.pdf'")
                .build());

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        PromptTemplate promptTemplate = new PromptTemplate(template);
        String systemPrompt = promptTemplate.render(Map.of("context", context));

        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .tools(routerTools)
                .call()
                .content();
    }
}
