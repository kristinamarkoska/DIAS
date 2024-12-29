package com.example.project1.controller;

import com.example.project1.dto.Response;
import com.example.project1.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/predict")
    public ResponseEntity<String> technicalAnalysis(@RequestParam(name = "companyId") Long companyId) {
        String response = aiService.technicalAnalysis(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/analyze")
    public ResponseEntity<Response> nlp(@RequestParam(name = "companyId") Long companyId) throws Exception {
        Response response = aiService.nlp(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/predict-next-month-price")
    public ResponseEntity<Double> lstm(@RequestParam(name = "companyId") Long companyId) {
        Double response = aiService.lstm(companyId);
        return ResponseEntity.ok(response);
    }
}
