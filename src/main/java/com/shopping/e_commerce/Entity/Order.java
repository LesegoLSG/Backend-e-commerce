package com.shopping.e_commerce.Entity;

import com.shopping.e_commerce.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing an Order in the e-commerce system.
 * An Order consists of multiple OrderItems, is associated with a User,
 * and includes details such as order date, total amount, and status.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate orderDate;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /**
     * One-to-many relationship with OrderItem entities.
     * Each Order can have multiple items, and deleting an Order will
     * also delete all its associated OrderItems.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    /**
     * Many-to-one relationship linking the Order to a specific User.
     * Each Order is placed by a single User.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
