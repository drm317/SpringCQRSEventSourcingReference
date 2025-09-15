package com.example.cqrs.command.order;

import com.example.cqrs.command.Command;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ConfirmOrderCommand implements Command {
    @NotNull
    private UUID orderId;
    
    public ConfirmOrderCommand() {
    }
    
    public ConfirmOrderCommand(UUID orderId) {
        this.orderId = orderId;
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
}