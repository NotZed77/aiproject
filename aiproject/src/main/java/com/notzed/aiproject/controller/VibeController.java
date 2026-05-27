package com.notzed.aiproject.controller;
import com.notzed.aiproject.service.AIService;
import com.notzed.aiproject.service.RAGService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match-vibe")
public class VibeController {

    private final AIService aiService;
    private final RAGService ragService;


    @GetMapping
    public String askChat(@RequestParam String feeling){
        return ragService.askAIForVibe(feeling);
    }
}
