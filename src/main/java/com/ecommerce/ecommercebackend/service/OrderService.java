package com.ecommerce.ecommercebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.ecommercebackend.entity.Order;
import com.ecommerce.ecommercebackend.entity.OrderItem;
import com.ecommerce.ecommercebackend.entity.Product;
import com.ecommerce.ecommercebackend.repository.OrderItemRepository;
import com.ecommerce.ecommercebackend.repository.OrderRepository;
import com.ecommerce.ecommercebackend.repository.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order placeOrder(Order order, List<OrderItem> items) {

        // Save order first
       order.setPaymentStatus("PAID");
       order.setOrderStatus("PLACED");

        Order savedOrder = orderRepository.save(order);

        // Process every product
        for (OrderItem item : items) {

            Product product = productRepository
                    .findById(item.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found: " + item.getProductId()
                            ));

            // Check stock
            if (product.getQuantity() == null ||
                    product.getQuantity() < item.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for: " + product.getTitle()
                );
            }

            // Decrease stock
            product.setQuantity(
                    product.getQuantity() - item.getQuantity()
            );

            productRepository.save(product);

            // Connect item to order
            item.setOrderId(savedOrder.getId());

            // Use current product price
            item.setPrice(product.getDiscountedPrice());

            item.setProductName(product.getTitle());

            orderItemRepository.save(item);
        }

        return savedOrder;
    }
}