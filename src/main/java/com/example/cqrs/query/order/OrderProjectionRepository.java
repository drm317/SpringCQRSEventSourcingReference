package com.example.cqrs.query.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderProjectionRepository extends JpaRepository<OrderProjection, UUID> {
    
    List<OrderProjection> findByCustomerNameContainingIgnoreCase(String customerName);
    
    List<OrderProjection> findByStatus(OrderProjectionStatus status);
    
    @Query("SELECT o FROM OrderProjection o WHERE o.customerName LIKE %:customerName% AND o.status = :status")
    List<OrderProjection> findByCustomerNameAndStatus(
        @Param("customerName") String customerName, 
        @Param("status") OrderProjectionStatus status);
}