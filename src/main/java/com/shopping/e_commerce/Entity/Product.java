package com.shopping.e_commerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
/**
 * Entity class representing a Product in the e-commerce system.
 * Contains details about the product, including pricing, inventory, category, images, and reviews.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String code;
    private BigDecimal price;
    private int inventory;

    /**
     * Many-to-One relationship with Category.
     * Each product belongs to a single category.
     * CascadeType.ALL is used to propagate all operations to the Category entity.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * One-to-Many relationship with Image.
     * Each product can have multiple images associated with it.
     * CascadeType.ALL allows all operations to cascade to related Image entities.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Image> images;

    /**
     * One-to-Many relationship with Review.
     * Each product can have multiple reviews, stored in a list.
     * CascadeType.ALL and orphanRemoval ensure review consistency when product is updated or deleted.
     */
    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews;

    /**
     * Parameterized constructor for creating a Product instance with all necessary fields.
     *
     * @param name        Product name.
     * @param brand       Brand of the product.
     * @param description Product description.
     * @param code        Product code.
     * @param price       Product price.
     * @param inventory   Inventory count.
     * @param category    Associated Category entity.
     */
    public Product(String name, String brand, String description, String code, BigDecimal price, int inventory, Category category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.code = code;
        this.price = price;
        this.inventory = inventory;
        this.category = category;
    }
}
