package com.shopping.e_commerce.dto.ProductDTO;

import com.shopping.e_commerce.Entity.Category;
import com.shopping.e_commerce.Entity.Image;
import com.shopping.e_commerce.dto.ImageDTO.ImageDto;
import com.shopping.e_commerce.dto.ReviewDto.ReviewDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String code;
    private BigDecimal price;
    private int inventory;

    private Category category;


    private List<ImageDto> images;

    private List<ReviewDto> reviews;
}
