package com.shopping.e_commerce.dto.ProductDTO;

import com.shopping.e_commerce.Entity.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String code;
    private BigDecimal price;
    private int inventory;
    private Category category;

}
