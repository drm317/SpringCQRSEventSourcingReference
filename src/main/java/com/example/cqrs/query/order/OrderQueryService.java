package com.example.cqrs.query.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {
    
    private final OrderProjectionRepository repository;
    
    public OrderQueryService(OrderProjectionRepository repository) {
        this.repository = repository;
    }
    
    public Optional<OrderProjection> findById(UUID orderId) {
        return repository.findById(orderId);
    }
    
    public List<OrderProjection> findAll() {
        return repository.findAll();
    }
    
    public List<OrderProjection> findByCustomerName(String customerName) {
        return repository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    public List<OrderProjection> findByStatus(OrderProjectionStatus status) {
        return repository.findByStatus(status);
    }
    
    public List<OrderProjection> findByCustomerNameAndStatus(String customerName, OrderProjectionStatus status) {
        return repository.findByCustomerNameAndStatus(customerName, status);
    }
}