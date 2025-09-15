package com.example.cqrs.api.order;

import jakarta.validation.constraints.NotBlank;

public class CreateOrderRequest {
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    public CreateOrderRequest() {
    }
    
    public CreateOrderRequest(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}