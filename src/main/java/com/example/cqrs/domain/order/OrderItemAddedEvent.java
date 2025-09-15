package com.example.cqrs.domain.order;

import com.example.cqrs.domain.DomainEvent;

import java.util.UUID;

public class OrderItemAddedEvent extends DomainEvent {
    private OrderItem item;
    
    public OrderItemAddedEvent() {
        super(null, 0);
    }
    
    public OrderItemAddedEvent(UUID orderId, long version, OrderItem item) {
        super(orderId, version);
        this.item = item;
    }
    
    public OrderItem getItem() {
        return item;
    }
    
    public void setItem(OrderItem item) {
        this.item = item;
    }
}