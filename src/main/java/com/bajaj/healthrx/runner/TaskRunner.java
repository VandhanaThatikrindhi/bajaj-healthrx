package com.bajaj.healthrx.runner;

import com.bajaj.healthrx.client.SolutionSubmissionClient;
import com.bajaj.healthrx.client.WebhookClient;
import com.bajaj.healthrx.dto.UserRequest;
import com.bajaj.healthrx.dto.WebhookResponse;
import com.bajaj.healthrx.service.impl.SqlSolutionService;
import com.bajaj.healthrx.util.RegNoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskRunner implements CommandLineRunner {

    private final WebhookClient webhookClient;
    private final SolutionSubmissionClient submissionClient;
    private final SqlSolutionService sqlService;

    private static final String NAME = "Vandhan Thandi";
    private static final String REG_NO = "22BRTD234";
    private static final String EMAIL = "vandhan.thatidi@gmail.com";

    @Override
    public void run(String... args) {

        System.out.println("STEP 1: Generating webhook...");
        UserRequest request = new UserRequest(NAME, REG_NO, EMAIL);
        WebhookResponse response = webhookClient.generateWebhook(request);

        boolean isOdd = RegNoUtil.isOdd(REG_NO);

        System.out.println("STEP 2: Solving SQL... (isOdd=" + isOdd + ")");
        String sql = sqlService.getSolution(isOdd);

        System.out.println("STEP 3: Submitting solution...");
        submissionClient.submitSolution(
                response.getWebhook(),
                response.getAccessToken(),
                sql);

        System.out.println("TASK COMPLETED");
    }
}
