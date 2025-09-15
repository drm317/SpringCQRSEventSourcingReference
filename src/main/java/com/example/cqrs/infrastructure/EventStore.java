package com.example.cqrs.infrastructure;

import com.example.cqrs.domain.DomainEvent;

import java.util.List;
import java.util.UUID;

public interface EventStore {
    void saveEvents(UUID aggregateId, List<DomainEvent> events, long expectedVersion);
    List<DomainEvent> getEventsForAggregate(UUID aggregateId);
    List<DomainEvent> getEventsForAggregate(UUID aggregateId, long fromVersion);
}