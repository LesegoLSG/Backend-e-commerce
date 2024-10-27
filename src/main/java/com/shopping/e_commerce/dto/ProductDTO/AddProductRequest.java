package com.shopping.e_commerce.dto.ProductDTO;

import com.shopping.e_commerce.Entity.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String code;
    private BigDecimal price;
    private int inventory;
    private Category category;


}
