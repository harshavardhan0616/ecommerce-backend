package com.ecommerce.ecommercebackend.controller;

import com.ecommerce.ecommercebackend.dto.OrderResponse;
import com.ecommerce.ecommercebackend.entity.Order;
import com.ecommerce.ecommercebackend.entity.OrderItem;
import com.ecommerce.ecommercebackend.repository.OrderItemRepository;
import com.ecommerce.ecommercebackend.repository.OrderRepository;
import com.ecommerce.ecommercebackend.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderController(
            OrderService orderService,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // ============================
    // PLACE ORDER
    // ============================
    @PostMapping
    public ResponseEntity<?> placeOrder(
            @RequestBody OrderRequest request) {

        try {

            Order savedOrder
                    = orderService.placeOrder(
                            request.getOrder(),
                            request.getItems()
                    );

            return ResponseEntity.ok(savedOrder);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ============================
    // GET USER ORDERS
    // ============================
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(
            @PathVariable Long userId) {

        try {

            List<Order> orders = 
            orderRepository.findByUserIdOrderByIdDesc(userId);

            List<OrderResponse> response
                    = new ArrayList<>();

            for (Order order : orders) {

                List<OrderItem> items
                        = orderItemRepository
                                .findByOrderId(order.getId());

                response.add(
                        new OrderResponse(order, items)
                );
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ============================
    // GET SINGLE ORDER
    // ============================
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(
            @PathVariable Long orderId) {

        try {

            Order order
                    = orderRepository
                            .findById(orderId)
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Order not found"
                                    )
                            );

            List<OrderItem> items
                    = orderItemRepository
                            .findByOrderId(orderId);

            return ResponseEntity.ok(
                    new OrderResponse(order, items)
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ============================
    // REQUEST CLASS
    // ============================
    public static class OrderRequest {

        private Order order;

        private List<OrderItem> items;

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
}
