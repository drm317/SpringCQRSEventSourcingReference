package com.example.cqrs.command.order;

import com.example.cqrs.domain.order.Order;
import com.example.cqrs.infrastructure.AggregateRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderCommandHandler {
    
    private final AggregateRepository<Order> orderRepository;
    
    public OrderCommandHandler(AggregateRepository<Order> orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public void handle(CreateOrderCommand command) {
        Order order = new Order(command.getOrderId(), command.getCustomerName());
        orderRepository.save(order);
    }
    
    public void handle(AddOrderItemCommand command) {
        Order order = orderRepository.findById(command.getOrderId(), Order.class)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + command.getOrderId()));
        
        order.addItem(command.getProductName(), command.getQuantity(), command.getUnitPrice());
        orderRepository.save(order);
    }
    
    public void handle(ConfirmOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId(), Order.class)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + command.getOrderId()));
        
        order.confirm();
        orderRepository.save(order);
    }
    
    public void handle(CancelOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId(), Order.class)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + command.getOrderId()));
        
        order.cancel();
        orderRepository.save(order);
    }
}