package com.example.cqrs.domain.order;

import com.example.cqrs.domain.DomainEvent;

import java.util.UUID;

public class OrderCreatedEvent extends DomainEvent {
    private String customerName;
    
    public OrderCreatedEvent() {
        super(null, 0);
    }
    
    public OrderCreatedEvent(UUID orderId, long version, String customerName) {
        super(orderId, version);
        this.customerName = customerName;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}