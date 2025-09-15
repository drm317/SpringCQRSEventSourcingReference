package com.example.cqrs.query.order;

import com.example.cqrs.domain.order.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventHandler {
    
    private final OrderProjectionRepository repository;
    
    public OrderEventHandler(OrderProjectionRepository repository) {
        this.repository = repository;
    }
    
    @EventListener
    public void on(OrderCreatedEvent event) {
        OrderProjection projection = new OrderProjection(event.getAggregateId(), event.getCustomerName());
        repository.save(projection);
    }
    
    @EventListener
    public void on(OrderItemAddedEvent event) {
        OrderProjection projection = repository.findById(event.getAggregateId())
            .orElseThrow(() -> new IllegalStateException("Order projection not found: " + event.getAggregateId()));
        
        projection.setTotalAmount(projection.getTotalAmount().add(event.getItem().getTotalPrice()));
        repository.save(projection);
    }
    
    @EventListener
    public void on(OrderConfirmedEvent event) {
        OrderProjection projection = repository.findById(event.getAggregateId())
            .orElseThrow(() -> new IllegalStateException("Order projection not found: " + event.getAggregateId()));
        
        projection.setStatus(OrderProjectionStatus.CONFIRMED);
        repository.save(projection);
    }
    
    @EventListener
    public void on(OrderCancelledEvent event) {
        OrderProjection projection = repository.findById(event.getAggregateId())
            .orElseThrow(() -> new IllegalStateException("Order projection not found: " + event.getAggregateId()));
        
        projection.setStatus(OrderProjectionStatus.CANCELLED);
        repository.save(projection);
    }
}