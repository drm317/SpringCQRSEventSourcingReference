package com.example.cqrs.api.order;

import com.example.cqrs.query.order.OrderProjection;
import com.example.cqrs.query.order.OrderProjectionStatus;
import com.example.cqrs.query.order.OrderQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderQueryController {
    
    private final OrderQueryService queryService;
    
    public OrderQueryController(OrderQueryService queryService) {
        this.queryService = queryService;
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderProjection> getOrder(@PathVariable UUID orderId) {
        return queryService.findById(orderId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<OrderProjection>> getAllOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) OrderProjectionStatus status) {
        
        List<OrderProjection> orders;
        
        if (customerName != null && status != null) {
            orders = queryService.findByCustomerNameAndStatus(customerName, status);
        } else if (customerName != null) {
            orders = queryService.findByCustomerName(customerName);
        } else if (status != null) {
            orders = queryService.findByStatus(status);
        } else {
            orders = queryService.findAll();
        }
        
        return ResponseEntity.ok(orders);
    }
}