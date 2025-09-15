package com.example.cqrs.domain.order;

import com.example.cqrs.domain.DomainEvent;

import java.util.UUID;

public class OrderCancelledEvent extends DomainEvent {
    
    public OrderCancelledEvent() {
        super(null, 0);
    }
    
    public OrderCancelledEvent(UUID orderId, long version) {
        super(orderId, version);
    }
}