package com.healthverse.aianalysis.controller;

import com.healthverse.aianalysis.dto.ChatRequest;
import com.healthverse.aianalysis.dto.ChatResponse;
import com.healthverse.aianalysis.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.Authentication;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis/chat")
public class ChatController {

    private final RestTemplate restTemplate;

    public ChatController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void checkAuth(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("User is not authenticated");
        }
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            Authentication authentication,
            @RequestBody Map<String, Object> payload) {
        checkAuth(authentication);

        ChatRequest request = new ChatRequest();
        if (payload.containsKey("message")) {
            request.setQuery(payload.get("message").toString());
        } else if (payload.containsKey("query")) {
            request.setQuery(payload.get("query").toString());
        } else {
            throw new BadRequestException("Chat request must contain 'message' or 'query'");
        }

        try {
            String aiAgentUrl = System.getenv("AI_AGENT_URL");
            if (aiAgentUrl == null || aiAgentUrl.isBlank()) {
                aiAgentUrl = "http://localhost:8000";
            }
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiAgentUrl + "/api/v1/agents/planning/orchestrate",
                    request,
                    Map.class
            );
            Map<String, Object> body = response.getBody();
            
            ChatResponse chatRes = new ChatResponse();
            if (body != null) {
                chatRes.setResponse((String) body.getOrDefault("response", "No response"));
                if (body.containsKey("agents_involved")) {
                    chatRes.setDisclaimer("Agents Involved: " + body.get("agents_involved"));
                } else {
                    chatRes.setDisclaimer("Multi-Agent Collaboration");
                }
            }
            chatRes.setRecommend_professional(false);
            return ResponseEntity.ok(chatRes);
        } catch (Exception e) {
            ChatResponse errResponse = new ChatResponse();
            errResponse.setResponse("Error connecting to AI Orchestrator: " + e.getMessage());
            errResponse.setDisclaimer("System Error");
            errResponse.setRecommend_professional(false);
            return ResponseEntity.status(500).body(errResponse);
        }
    }
}
