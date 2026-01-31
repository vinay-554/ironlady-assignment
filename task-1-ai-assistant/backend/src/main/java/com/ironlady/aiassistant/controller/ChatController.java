package com.ironlady.aiassistant.controller;

import com.ironlady.aiassistant.dto.ChatRequest;
import com.ironlady.aiassistant.service.OpenAIServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChatController {

    @Autowired
    private OpenAIServiceUtil openAIServiceUtil;

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return openAIServiceUtil.getAIResponse(request.getMessage());
    }

}


