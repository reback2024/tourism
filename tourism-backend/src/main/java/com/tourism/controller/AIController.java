package com.tourism.controller;

import com.tourism.common.Result;
import com.tourism.service.ZhipuAiService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final ZhipuAiService aiService;
    public AIController(ZhipuAiService aiService) { this.aiService = aiService; }

    @PostMapping("/chat")
    public Result<Map<String, String>> chat(@RequestBody Map<String, Object> body) {
        String message = (String) body.getOrDefault("message", "");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> history = (List<Map<String, String>>) body.get("history");
        String reply = aiService.chat(message, history);
        return Result.ok(Map.of("reply", reply));
    }

    @PostMapping("/generate-itinerary")
    public Result<Map<String, String>> generateItinerary(@RequestBody Map<String, Object> body) {
        String destination = (String) body.getOrDefault("destination", "");
        int days = body.get("days") instanceof Integer ? (int) body.get("days") : 3;
        double budget = body.get("budget") instanceof Number ? ((Number) body.get("budget")).doubleValue() : 3000;
        String preferences = (String) body.getOrDefault("preferences", "");
        String content = aiService.generateItinerary(destination, days, budget, preferences);
        return Result.ok(Map.of("content", content, "destination", destination, "days", String.valueOf(days), "budget", String.valueOf((int) budget), "preferences", preferences));
    }
}
