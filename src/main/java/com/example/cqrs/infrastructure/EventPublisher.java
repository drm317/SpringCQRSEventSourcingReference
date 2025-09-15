package com.example.cqrs.infrastructure;

import com.example.cqrs.domain.AggregateRoot;
import com.example.cqrs.domain.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    public void publishEvents(AggregateRoot aggregate) {
        for (DomainEvent event : aggregate.getUncommittedEvents()) {
            applicationEventPublisher.publishEvent(event);
        }
    }
}