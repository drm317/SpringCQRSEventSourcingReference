package com.example.cqrs.command.order;

import com.example.cqrs.command.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateOrderCommand implements Command {
    @NotNull
    private UUID orderId;
    
    @NotBlank
    private String customerName;
    
    public CreateOrderCommand() {
    }
    
    public CreateOrderCommand(UUID orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
    }
    
    @Override
    public UUID getAggregateId() {
        return orderId;
    }
    
    public UUID getOrderId() {
        return orderId;
    }
    
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}