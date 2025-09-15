package com.example.cqrs.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = com.example.cqrs.domain.order.OrderCreatedEvent.class, name = "OrderCreated"),
    @JsonSubTypes.Type(value = com.example.cqrs.domain.order.OrderItemAddedEvent.class, name = "OrderItemAdded"),
    @JsonSubTypes.Type(value = com.example.cqrs.domain.order.OrderConfirmedEvent.class, name = "OrderConfirmed"),
    @JsonSubTypes.Type(value = com.example.cqrs.domain.order.OrderCancelledEvent.class, name = "OrderCancelled")
})
public abstract class DomainEvent {
    private final UUID eventId;
    private final UUID aggregateId;
    private final long aggregateVersion;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime occurredOn;
    
    protected DomainEvent(UUID aggregateId, long aggregateVersion) {
        this.eventId = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.aggregateVersion = aggregateVersion;
        this.occurredOn = LocalDateTime.now();
    }
    
    public UUID getEventId() {
        return eventId;
    }
    
    public UUID getAggregateId() {
        return aggregateId;
    }
    
    public long getAggregateVersion() {
        return aggregateVersion;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}