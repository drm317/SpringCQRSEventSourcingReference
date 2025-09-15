package com.example.cqrs.integration;

import com.example.cqrs.api.order.CreateOrderRequest;
import com.example.cqrs.api.order.AddOrderItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCompleteOrderLifecycle() throws Exception {
        String baseUrl = "http://localhost:" + port + "/api/orders";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // 1. Create an order
        CreateOrderRequest createRequest = new CreateOrderRequest("John Doe");
        HttpEntity<CreateOrderRequest> createEntity = new HttpEntity<>(createRequest, headers);
        
        ResponseEntity<String> createResponse = restTemplate.postForEntity(baseUrl, createEntity, String.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        
        String responseContent = createResponse.getBody();
        assertNotNull(responseContent);
        assertTrue(responseContent.contains("Order created successfully"));
        
        String orderIdStr = extractOrderIdFromResponse(responseContent);
        UUID orderId = UUID.fromString(orderIdStr);
        
        // 2. Add item to order
        AddOrderItemRequest itemRequest = new AddOrderItemRequest("Laptop", 2, new BigDecimal("999.99"));
        HttpEntity<AddOrderItemRequest> itemEntity = new HttpEntity<>(itemRequest, headers);
        
        ResponseEntity<String> itemResponse = restTemplate.postForEntity(
            baseUrl + "/" + orderId + "/items", itemEntity, String.class);
        assertEquals(HttpStatus.OK, itemResponse.getStatusCode());
        
        // Wait a bit for event processing
        Thread.sleep(100);
        
        // 3. Get order and verify
        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl + "/" + orderId, String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        
        String orderJson = getResponse.getBody();
        assertNotNull(orderJson);
        assertTrue(orderJson.contains("John Doe"));
        assertTrue(orderJson.contains("PENDING"));
        
        // 4. Confirm order
        ResponseEntity<String> confirmResponse = restTemplate.postForEntity(
            baseUrl + "/" + orderId + "/confirm", new HttpEntity<>(headers), String.class);
        assertEquals(HttpStatus.OK, confirmResponse.getStatusCode());
        
        // Wait a bit for event processing
        Thread.sleep(100);
        
        // 5. Verify order is confirmed
        ResponseEntity<String> finalGetResponse = restTemplate.getForEntity(baseUrl + "/" + orderId, String.class);
        assertEquals(HttpStatus.OK, finalGetResponse.getStatusCode());
        
        String finalOrderJson = finalGetResponse.getBody();
        assertNotNull(finalOrderJson);
        assertTrue(finalOrderJson.contains("CONFIRMED"));
    }

    private String extractOrderIdFromResponse(String responseContent) throws Exception {
        return objectMapper.readTree(responseContent)
                .get("orderId")
                .asText();
    }
}