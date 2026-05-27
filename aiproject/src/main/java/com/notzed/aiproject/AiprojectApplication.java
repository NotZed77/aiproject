package com.notzed.aiproject;

import com.notzed.aiproject.service.RAGService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AiprojectApplication implements CommandLineRunner {

	@Autowired
	private RAGService ragService;

	@Autowired
	private VectorStore vectorStore;

	public static void main(String[] args) {
		SpringApplication.run(AiprojectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Document> existing = vectorStore.similaritySearch(
				SearchRequest.builder()
						.query("remote work expense policy")
						.topK(1)
						.similarityThreshold(0.0)
						.filterExpression("file_name == 'policy.pdf'")
						.build()
		);

		if (existing.isEmpty()) {
			System.out.println("Ingesting policy.pdf");
			ragService.ingestVectorStore();
			System.out.println("Policy PDF ingested");
		} else {
			System.out.println("Already ingested, skipping");
		}
	}
}
