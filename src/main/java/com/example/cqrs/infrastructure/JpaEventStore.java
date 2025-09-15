package com.example.cqrs.infrastructure;

import com.example.cqrs.domain.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class JpaEventStore implements EventStore {
    
    private final EventStoreRepository repository;
    private final ObjectMapper objectMapper;
    
    public JpaEventStore(EventStoreRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void saveEvents(UUID aggregateId, List<DomainEvent> events, long expectedVersion) {
        Long currentVersion = repository.findMaxVersionByAggregateId(aggregateId);
        
        if (currentVersion != null && currentVersion != expectedVersion) {
            throw new OptimisticLockingException(
                String.format("Expected version %d but was %d for aggregate %s", 
                    expectedVersion, currentVersion, aggregateId));
        }
        
        List<EventStoreEntity> entities = events.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
            
        repository.saveAll(entities);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> getEventsForAggregate(UUID aggregateId) {
        return repository.findByAggregateIdOrderByAggregateVersionAsc(aggregateId)
            .stream()
            .map(this::fromEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> getEventsForAggregate(UUID aggregateId, long fromVersion) {
        return repository.findByAggregateIdAndAggregateVersionGreaterThanOrderByAggregateVersionAsc(aggregateId, fromVersion)
            .stream()
            .map(this::fromEntity)
            .collect(Collectors.toList());
    }
    
    private EventStoreEntity toEntity(DomainEvent event) {
        try {
            String eventData = objectMapper.writeValueAsString(event);
            return new EventStoreEntity(
                event.getEventId(),
                event.getAggregateId(),
                event.getAggregateVersion(),
                event.getClass().getSimpleName(),
                eventData,
                event.getOccurredOn()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
    
    private DomainEvent fromEntity(EventStoreEntity entity) {
        try {
            return objectMapper.readValue(entity.getEventData(), DomainEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}