package com.shopping.e_commerce.dto.CartDTO;

import com.shopping.e_commerce.dto.ProductDTO.ProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
