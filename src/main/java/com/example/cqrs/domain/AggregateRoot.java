package com.example.cqrs.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class AggregateRoot {
    private UUID id;
    private long version;
    private List<DomainEvent> uncommittedEvents = new ArrayList<>();
    
    protected AggregateRoot() {
    }
    
    protected AggregateRoot(UUID id) {
        this.id = id;
        this.version = 0;
    }
    
    public UUID getId() {
        return id;
    }
    
    public long getVersion() {
        return version;
    }
    
    protected void setId(UUID id) {
        this.id = id;
    }
    
    protected void setVersion(long version) {
        this.version = version;
    }
    
    protected void apply(DomainEvent event) {
        uncommittedEvents.add(event);
        version++;
        when(event);
    }
    
    public void replay(DomainEvent event) {
        when(event);
        version = event.getAggregateVersion();
    }
    
    public List<DomainEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }
    
    public void markEventsAsCommitted() {
        uncommittedEvents.clear();
    }
    
    protected abstract void when(DomainEvent event);
}