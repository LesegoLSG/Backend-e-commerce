package com.shopping.e_commerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a Review for a product in the e-commerce system.
 * Contains details about the review including the rating, summary, and message.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String name;
    private String summary;
    private String message;

    /**
     * Many-to-One relationship with Product.
     * Each review is associated with a single product.
     * JsonIgnore prevents circular reference issues during JSON serialization.
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name= "product_id")
    private Product product;

    /**
     * Many-to-One relationship with User.
     * Each review is associated with a single user who wrote it.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
