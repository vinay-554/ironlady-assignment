package com.ironlady.aiassistant.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAIServiceUtil {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String SYSTEM_PROMPT = """
        You are Iron Lady – an AI Guidance Assistant for women professionals.
        
        YOUR ROLE:
        - Guide users through Iron Lady’s programs and journey
        - Help them choose the right next step
        - Act like a friendly mentor + business assistant
        
        STRICT RULES:
        - Keep responses SHORT (4–6 lines max)
        - Use bullet points
        - Ask only ONE question at a time
        - NEVER write long paragraphs
        
        BUSINESS AWARENESS:
        Iron Lady offers:
        - Leadership development programs
        - Mentorship & coaching for women professionals
        - Confidence, communication, and career growth support
        
        WHENEVER RELEVANT:
        - Suggest Iron Lady programs
        - Suggest “Contact Us” for personalized guidance
        - Suggest next actions clearly
        
        END EVERY RESPONSE WITH ONE OPTION-BASED QUESTION.
        Example:
        • Want to explore our programs?
        • Want to talk to our team?
        • Want a quick self-assessment?
        
        Tone: Warm, supportive, empowering.
        """;


    public String getAIResponse(String userMessage) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> systemMsg = Map.of(
                    "role", "system",
                    "content", SYSTEM_PROMPT
            );

            Map<String, Object> userMsg = Map.of(
                    "role", "user",
                    "content", userMessage
            );

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.1-8b-instant"); //replaced
            body.put("messages", List.of(systemMsg, userMsg));

            var headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + groqApiKey);
            headers.set("Content-Type", "application/json");

            var request = new org.springframework.http.HttpEntity<>(body, headers);

            Map response = restTemplate.postForObject(GROQ_URL, request, Map.class);

            List choices = (List) response.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while calling Groq AI: " + e.getMessage();
        }
    }
}


//package com.ironlady.aiassistant.service;
//
//import com.theokanning.openai.completion.chat.ChatCompletionRequest;
//import com.theokanning.openai.completion.chat.ChatCompletionResult;
//import com.theokanning.openai.completion.chat.ChatMessage;
//import com.theokanning.openai.service.OpenAiService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class OpenAIServiceUtil {
//
//    @Value("${openai.api.key}")
//    private String apiKey;
//
//    private static final String SYSTEM_PROMPT = """
//    You are an AI-powered Program Guidance Assistant for Iron Lady.
//
//    Iron Lady is a leadership and empowerment organization focused on women professionals and aspiring women leaders.
//    You help users understand programs, choose the right path, and guide them on next steps.
//    Be empathetic, professional, and encouraging.
//    """;
//
//    public String getAIResponse(String userMessage) {
//        try {
//            OpenAiService service = new OpenAiService(apiKey);
//
//            List<ChatMessage> messages = new ArrayList<>();
//            messages.add(new ChatMessage("system", SYSTEM_PROMPT));
//            messages.add(new ChatMessage("user", userMessage));
//
//            ChatCompletionRequest request = ChatCompletionRequest.builder()
//                    .model("gpt-3.5-turbo")
//                    .messages(messages)
//                    .maxTokens(200)
//                    .build();
//
//            ChatCompletionResult result = service.createChatCompletion(request);
//            return result.getChoices().get(0).getMessage().getContent();
//
//        } catch (Exception e) {
//            if (e.getMessage().contains("quota")) {
//                return "Based on your experience, Iron Lady’s leadership programs are designed to help working professionals build confidence, strategic thinking, and leadership skills. You can explore programs focused on career growth and empowerment.";
//            }
//            return "Error while calling AI: " + e.getMessage();
//        }
//
//    }
//}
//
