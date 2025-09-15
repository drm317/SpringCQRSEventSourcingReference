package com.example.cqrs.infrastructure;

import com.example.cqrs.domain.AggregateRoot;
import com.example.cqrs.domain.DomainEvent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AggregateRepository<T extends AggregateRoot> {
    
    private final EventStore eventStore;
    private final EventPublisher eventPublisher;
    
    public AggregateRepository(EventStore eventStore, EventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }
    
    public void save(T aggregate) {
        List<DomainEvent> uncommittedEvents = aggregate.getUncommittedEvents();
        if (!uncommittedEvents.isEmpty()) {
            eventStore.saveEvents(aggregate.getId(), uncommittedEvents, aggregate.getVersion() - uncommittedEvents.size());
            eventPublisher.publishEvents(aggregate);
            aggregate.markEventsAsCommitted();
        }
    }
    
    public Optional<T> findById(UUID aggregateId, Class<T> aggregateType) {
        try {
            List<DomainEvent> events = eventStore.getEventsForAggregate(aggregateId);
            if (events.isEmpty()) {
                return Optional.empty();
            }
            
            T aggregate = aggregateType.getDeclaredConstructor().newInstance();
            events.forEach(aggregate::replay);
            
            return Optional.of(aggregate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reconstruct aggregate", e);
        }
    }
}