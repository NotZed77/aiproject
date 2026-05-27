package com.notzed.aiproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/poem")
public class PoemController {

    private final ChatClient chatClient;

    @GetMapping
    public String chat(@RequestParam String topic, @RequestParam String lang){
        String systemPrompt = """
                You are a friendly poem generation assistant.
                Try to be as creative as possible.
                Give the title of the poem followed by poem text and lastly rhyme scheme of the poem (e.g., AABB) 
                Always respond in %s.
                """.formatted(lang);

        String userPrompt = "Write a poem about: " + topic;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

    }
}
