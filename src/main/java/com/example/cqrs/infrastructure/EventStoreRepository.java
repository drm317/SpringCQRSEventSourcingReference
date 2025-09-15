package com.example.cqrs.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventStoreRepository extends JpaRepository<EventStoreEntity, Long> {
    
    @Query("SELECT e FROM EventStoreEntity e WHERE e.aggregateId = :aggregateId ORDER BY e.aggregateVersion ASC")
    List<EventStoreEntity> findByAggregateIdOrderByAggregateVersionAsc(@Param("aggregateId") UUID aggregateId);
    
    @Query("SELECT e FROM EventStoreEntity e WHERE e.aggregateId = :aggregateId AND e.aggregateVersion > :fromVersion ORDER BY e.aggregateVersion ASC")
    List<EventStoreEntity> findByAggregateIdAndAggregateVersionGreaterThanOrderByAggregateVersionAsc(
            @Param("aggregateId") UUID aggregateId, 
            @Param("fromVersion") Long fromVersion);
    
    @Query("SELECT MAX(e.aggregateVersion) FROM EventStoreEntity e WHERE e.aggregateId = :aggregateId")
    Long findMaxVersionByAggregateId(@Param("aggregateId") UUID aggregateId);
}