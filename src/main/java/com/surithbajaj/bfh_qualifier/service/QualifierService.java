package com.surithbajaj.bfh_qualifier.service;

import com.surithbajaj.bfh_qualifier.dto.SolutionRequest;
import com.surithbajaj.bfh_qualifier.dto.WebhookRequest;
import com.surithbajaj.bfh_qualifier.dto.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QualifierService implements CommandLineRunner {

    @Autowired
    private RestTemplate restTemplate;

    private static final String WEBHOOK_GENERATE_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String WEBHOOK_SUBMIT_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("=== Starting Bajaj Finserv Health Qualifier ===");
            System.out.println("Student: Surith L G");
            System.out.println("Registration: 22BLC1247 (Odd - Question 1)");

            WebhookResponse webhookResponse = generateWebhook();
            
            if (webhookResponse != null) {
                System.out.println("✓ Webhook generated successfully!");
                System.out.println("Webhook URL: " + webhookResponse.getWebhook());
                
                String accessToken = webhookResponse.getAccessToken();
                if (accessToken != null && !accessToken.isEmpty()) {
                    System.out.println("✓ Access Token received (length: " + accessToken.length() + ")");
                    System.out.println("Token preview: " + accessToken.substring(0, Math.min(20, accessToken.length())) + "...");
                } else {
                    System.err.println("❌ No access token received!");
                    return;
                }
                
                String sqlQuery = getQuestion1Solution();
                System.out.println("✓ SQL Query prepared for submission");
                
                submitSolution(sqlQuery, accessToken);
                
                System.out.println("✓ Solution submitted successfully!");
                System.out.println("=== Qualifier process completed ===");
            } else {
                System.err.println("❌ Failed to generate webhook. Cannot proceed.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error in qualifier process: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private WebhookResponse generateWebhook() {
        try {
            System.out.println("📡 Generating webhook...");
            
            WebhookRequest request = new WebhookRequest("Surith L G", "22BLC1247", "surithcodes204@gmail.com");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
            
            WebhookResponse response = restTemplate.postForObject(WEBHOOK_GENERATE_URL, entity, WebhookResponse.class);
            
            return response;
        } catch (Exception e) {
            System.err.println("❌ Error generating webhook: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String getQuestion1Solution() {
        // Find highest salary NOT paid on 1st day of any month
        return """
            SELECT 
                p.AMOUNT as SALARY,
                CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME,
                TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) as AGE,
                d.DEPARTMENT_NAME
            FROM PAYMENTS p
            INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
            INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            WHERE DAY(p.PAYMENT_TIME) != 1
            AND p.AMOUNT = (
                SELECT MAX(p2.AMOUNT)
                FROM PAYMENTS p2
                WHERE DAY(p2.PAYMENT_TIME) != 1
            )
            LIMIT 1;
            """;
    }

    private void submitSolution(String sqlQuery, String accessToken) {
        try {
            System.out.println("📤 Submitting solution...");
            System.out.println("Using access token: " + accessToken.substring(0, Math.min(30, accessToken.length())) + "...");
            
            SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken); 
            
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);
            
            System.out.println("📋 Request Headers: " + headers.toString());
            System.out.println("📋 Request Body: " + solutionRequest.getFinalQuery());
            
            String response = restTemplate.exchange(WEBHOOK_SUBMIT_URL, HttpMethod.POST, entity, String.class).getBody();
            
            System.out.println("📋 Final SQL Query submitted:");
            System.out.println(sqlQuery.trim());
            System.out.println("📨 Server response: " + response);
            
        } catch (Exception e) {
            System.err.println("❌ Error submitting solution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

