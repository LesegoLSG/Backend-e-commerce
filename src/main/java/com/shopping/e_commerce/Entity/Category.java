package com.shopping.e_commerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entity class representing a category of products in the e-commerce system.
 * Each category has a name and can contain multiple products.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    /**
     * List of products associated with this category.
     * The @JsonIgnore annotation prevents these details from appearing in JSON responses,
     * helping to avoid circular references when serialized.
     */
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    /**
     * Convenience constructor for creating a category with a specific name.
     * Useful when only the name needs to be set during initialization.
     *
     * @param name The name of the category
     */
    public Category(String name) {
        this.name = name;
    }

}
