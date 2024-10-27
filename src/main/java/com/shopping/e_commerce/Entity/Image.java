package com.shopping.e_commerce.Entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

/**
 * Entity class representing an image associated with a product in the e-commerce system.
 * Each image has a filename, file type, and a reference to the product it belongs to.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;

    /**
     * Binary data representing the image itself.
     * Stored as a Blob due to potentially large data size.
     */
    @Lob
    private Blob image;
    private String downloadUrl;

    /**
     * Many-to-one relationship indicating that an image is associated with a single product.
     * The `product_id` column will store the foreign key reference to the related product.
     */
    @ManyToOne
    @JoinColumn(name= "product_id")
    private Product product;
}
