package com.ecommerce.ecommercebackend.dto;

import java.util.List;

import com.ecommerce.ecommercebackend.entity.Order;
import com.ecommerce.ecommercebackend.entity.OrderItem;

public class OrderResponse {

    private Order order;
    private List<OrderItem> items;

    public OrderResponse() {
    }

    public OrderResponse(Order order, List<OrderItem> items) {
        this.order = order;
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}