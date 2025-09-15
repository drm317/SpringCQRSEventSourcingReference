package com.example.cqrs.domain.order;

import com.example.cqrs.domain.DomainEvent;

import java.util.UUID;

public class OrderConfirmedEvent extends DomainEvent {
    
    public OrderConfirmedEvent() {
        super(null, 0);
    }
    
    public OrderConfirmedEvent(UUID orderId, long version) {
        super(orderId, version);
    }
}