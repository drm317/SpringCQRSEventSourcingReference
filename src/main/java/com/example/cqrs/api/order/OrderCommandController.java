package com.example.cqrs.api.order;

import com.example.cqrs.command.order.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderCommandController {
    
    private final OrderCommandHandler commandHandler;
    
    public OrderCommandController(OrderCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        UUID orderId = UUID.randomUUID();
        CreateOrderCommand command = new CreateOrderCommand(orderId, request.getCustomerName());
        
        commandHandler.handle(command);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new OrderResponse(orderId, "Order created successfully"));
    }
    
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponse> addOrderItem(
            @PathVariable UUID orderId,
            @Valid @RequestBody AddOrderItemRequest request) {
        
        AddOrderItemCommand command = new AddOrderItemCommand(
            orderId, 
            request.getProductName(), 
            request.getQuantity(), 
            request.getUnitPrice()
        );
        
        commandHandler.handle(command);
        
        return ResponseEntity.ok(new OrderResponse(orderId, "Item added successfully"));
    }
    
    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable UUID orderId) {
        ConfirmOrderCommand command = new ConfirmOrderCommand(orderId);
        commandHandler.handle(command);
        
        return ResponseEntity.ok(new OrderResponse(orderId, "Order confirmed successfully"));
    }
    
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID orderId) {
        CancelOrderCommand command = new CancelOrderCommand(orderId);
        commandHandler.handle(command);
        
        return ResponseEntity.ok(new OrderResponse(orderId, "Order cancelled successfully"));
    }
}