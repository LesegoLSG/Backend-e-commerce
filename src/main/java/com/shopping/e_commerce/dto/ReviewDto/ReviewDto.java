package com.shopping.e_commerce.dto.ReviewDto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private int rating;
    private String name;
    private String summary;
    private String message;
}
