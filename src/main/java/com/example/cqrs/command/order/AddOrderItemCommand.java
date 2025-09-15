package com.example.cqrs.command.order;

import com.example.cqrs.command.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class AddOrderItemCommand implements Command {
    @NotNull
    private UUID orderId;
    
    @NotBlank
    private String productName;
    
    @Positive
    private int quantity;
    
    @NotNull
    @Positive
    private BigDecimal unitPrice;
    
    public AddOrderItemCommand() {
    }
    
    public AddOrderItemCommand(UUID orderId, String productName, int quantity, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}