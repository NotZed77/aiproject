package com.notzed.aiproject.controller;

import com.notzed.aiproject.dto.ChatDto;
import com.notzed.aiproject.service.RAGService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final RAGService ragService;

    @PostMapping
    public String chat(@RequestParam String userId, @RequestBody ChatDto request){
        return ragService.chatWithAI(request, userId);
    }

    @PostMapping("/stocks")
    public String chatForStocks(@RequestParam String question){
        return ragService.chatWithAIForStocks(question);
    }

    @PostMapping("/router")
    public String routerChat(@RequestParam String ques){
        return ragService.askAIAboutManual(ques);
    }



}
