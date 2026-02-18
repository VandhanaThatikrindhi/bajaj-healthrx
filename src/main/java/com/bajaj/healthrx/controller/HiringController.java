package com.bajaj.healthrx.controller;

import com.bajaj.healthrx.client.SolutionSubmissionClient;
import com.bajaj.healthrx.client.WebhookClient;
import com.bajaj.healthrx.dto.SolutionSubmission;
import com.bajaj.healthrx.dto.UserRequest;
import com.bajaj.healthrx.dto.WebhookResponse;
import com.bajaj.healthrx.service.impl.SqlSolutionService;
import com.bajaj.healthrx.util.RegNoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/hiring")
@Tag(name = "Hiring API", description = "Endpoints to interact with BFH Hiring Webhook")
public class HiringController {

    @Autowired
    private WebhookClient webhookClient;

    @Autowired
    private SqlSolutionService sqlSolutionService;

    @Autowired
    private RestTemplate restTemplate;

    // Store last generated response so we can use it across requests
    private WebhookResponse lastWebhookResponse;
    private String lastRegNo;

    @PostMapping("/generateWebhook")
    @Operation(summary = "Step 1: Generate Webhook & Token", description = "Pass your name, regNo, email as JSON body. Returns webhook URL + accessToken.")
    public WebhookResponse generateWebhook(@RequestBody UserRequest userRequest) {
        lastWebhookResponse = webhookClient.generateWebhook(userRequest);
        lastRegNo = userRequest.getRegNo();
        return lastWebhookResponse;
    }

    @PostMapping("/submitSolution")
    @Operation(summary = "Step 2: Submit SQL Solution", description = "Pass { \"finalQuery\": \"YOUR SQL\" }. Submits to the webhook URL with JWT from Step 1.")
    public Map<String, Object> submitSolution(@RequestBody SolutionSubmission submission) {

        if (lastWebhookResponse == null) {
            return Map.of("error", "No webhook generated yet. Call /generateWebhook first.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", lastWebhookResponse.getAccessToken());

        HttpEntity<SolutionSubmission> entity = new HttpEntity<>(submission, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                lastWebhookResponse.getWebhook(),
                HttpMethod.POST,
                entity,
                String.class);

        return Map.of(
                "webhookUrl", lastWebhookResponse.getWebhook(),
                "statusCode", response.getStatusCode().toString(),
                "response", response.getBody() != null ? response.getBody() : "No response body");
    }

    @GetMapping("/viewSolution")
    @Operation(summary = "View: See the SQL solution assigned based on regNo", description = "Shows the SQL query based on odd/even logic of the regNo used in Step 1.")
    public Map<String, Object> viewSolution() {
        String regNo = lastRegNo != null ? lastRegNo : "REG12347";
        boolean isOdd = RegNoUtil.isOdd(regNo);
        String sql = sqlSolutionService.getSolution(isOdd);

        return Map.of(
                "regNo", regNo,
                "lastTwoDigits", regNo.replaceAll("\\D", "").substring(regNo.replaceAll("\\D", "").length() - 2),
                "isOdd", isOdd,
                "questionAssigned", isOdd ? "Question 1 (Odd)" : "Question 2 (Even)",
                "sqlQuery", sql,
                "lastWebhookResponse", lastWebhookResponse != null ? lastWebhookResponse : "Not generated yet");
    }
}
