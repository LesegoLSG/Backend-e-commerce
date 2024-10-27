package com.shopping.e_commerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entity class representing an individual item in an Order.
 * Each OrderItem corresponds to a single product within an Order,
 * tracking quantity and price.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal price;

    /**
     * Many-to-one relationship with the Order entity.
     * Each OrderItem is part of one Order, but an Order can have multiple OrderItems.
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * Many-to-one relationship with the Product entity.
     * Each OrderItem corresponds to a single Product.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Constructor to initialize an OrderItem with quantity, price, order, and product.
     *
     * @param quantity Number of units for the product in the order.
     * @param price    Price per unit of the product.
     * @param order    Associated Order entity.
     * @param product  Associated Product entity.
     */
    public OrderItem(int quantity, BigDecimal price, Order order, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.product = product;
    }
}
