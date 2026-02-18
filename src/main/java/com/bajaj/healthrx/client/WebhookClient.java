package com.bajaj.healthrx.client;

import com.bajaj.healthrx.dto.UserRequest;
import com.bajaj.healthrx.dto.WebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WebhookClient {

    private final RestTemplate restTemplate;

    private static final String GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public WebhookResponse generateWebhook(UserRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                GENERATE_URL,
                HttpMethod.POST,
                entity,
                WebhookResponse.class);

        return response.getBody();
    }
}
