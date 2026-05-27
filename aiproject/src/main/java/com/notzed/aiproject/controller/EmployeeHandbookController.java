package com.notzed.aiproject.controller;

import com.notzed.aiproject.service.RAGService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/handbook")
public class EmployeeHandbookController {

    private final ChatClient chatClient;
    private final RAGService ragService;

    @GetMapping
    public String chat(@RequestParam String question){
        return ragService.askAIForHandbook(question);
    }



}
