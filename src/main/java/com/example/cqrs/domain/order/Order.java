package com.example.cqrs.domain.order;

import com.example.cqrs.domain.AggregateRoot;
import com.example.cqrs.domain.DomainEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot {
    private String customerName;
    private OrderStatus status;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    
    public Order() {
        super();
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }
    
    public Order(UUID orderId, String customerName) {
        super(orderId);
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
        apply(new OrderCreatedEvent(orderId, getVersion() + 1, customerName));
    }
    
    public void addItem(String productName, int quantity, BigDecimal unitPrice) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot add items to a non-pending order");
        }
        
        OrderItem item = new OrderItem(productName, quantity, unitPrice);
        apply(new OrderItemAddedEvent(getId(), getVersion() + 1, item));
    }
    
    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm an order without items");
        }
        
        apply(new OrderConfirmedEvent(getId(), getVersion() + 1));
    }
    
    public void cancel() {
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }
        
        apply(new OrderCancelledEvent(getId(), getVersion() + 1));
    }
    
    @Override
    protected void when(DomainEvent event) {
        switch (event) {
            case OrderCreatedEvent e -> {
                this.setId(e.getAggregateId());
                this.customerName = e.getCustomerName();
                this.status = OrderStatus.PENDING;
            }
            case OrderItemAddedEvent e -> {
                this.items.add(e.getItem());
                this.totalAmount = this.totalAmount.add(e.getItem().getTotalPrice());
            }
            case OrderConfirmedEvent e -> {
                this.status = OrderStatus.CONFIRMED;
            }
            case OrderCancelledEvent e -> {
                this.status = OrderStatus.CANCELLED;
            }
            default -> {
                // Unknown event type
            }
        }
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}