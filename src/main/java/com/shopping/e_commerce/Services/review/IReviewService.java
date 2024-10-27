package com.shopping.e_commerce.Services.review;

import com.shopping.e_commerce.Entity.Review;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.dto.ReviewDto.ReviewDto;
/**
 * Interface defining the operations for managing reviews in the e-commerce application.
 */
public interface IReviewService {

    Review addReviewToProduct(ReviewDto reviewDto, Long productId,User user);

    void deleteReviewById(Long reviewId);

//    public ReviewDto convertToDto(Review review);
}
