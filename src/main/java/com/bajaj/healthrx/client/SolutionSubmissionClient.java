package com.bajaj.healthrx.client;

import com.bajaj.healthrx.dto.SolutionSubmission;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SolutionSubmissionClient {

    private final RestTemplate restTemplate;

    public void submitSolution(String webhookUrl, String token, String sql) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        SolutionSubmission body = new SolutionSubmission(sql);

        HttpEntity<SolutionSubmission> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                webhookUrl,
                HttpMethod.POST,
                entity,
                String.class);

        System.out.println("SUBMISSION RESPONSE: " + response.getBody());
    }
}
