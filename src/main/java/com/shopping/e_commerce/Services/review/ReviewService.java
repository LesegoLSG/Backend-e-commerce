package com.shopping.e_commerce.Services.review;

import com.shopping.e_commerce.Entity.Product;
import com.shopping.e_commerce.Entity.Review;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Repository.ProductRepository;
import com.shopping.e_commerce.Repository.ReviewRepository;
import com.shopping.e_commerce.Services.product.IProductService;
import com.shopping.e_commerce.dto.ReviewDto.ReviewDto;
import com.shopping.e_commerce.exceptions.ProductNotFoundException;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing reviews associated with products.
 */
@RequiredArgsConstructor
@Service
public class ReviewService implements IReviewService{
    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final ProductRepository productRepository;

    /**
     * Adds a review to a specific product.
     *
     * @param reviewDto the DTO containing review details
     * @param productId the ID of the product to review
     * @param user the user who is submitting the review
     * @return the saved Review object
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public Review addReviewToProduct(ReviewDto reviewDto, Long productId,User user) {
        // Retrieve the product by its ID; throws exception if not found
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        System.out.println("Product:" + product);
        try {
            // Create a new Review object and populate its fields
            Review review = new Review();
            review.setRating(reviewDto.getRating());
            review.setName(reviewDto.getName());
            review.setSummary(reviewDto.getSummary());
            review.setMessage(reviewDto.getMessage());
            review.setProduct(product);
            System.out.println("Review"+review);
            review.setUser(user);
            return reviewRepository.save(review);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Deletes a review by its ID.
     *
     * @param reviewId the ID of the review to delete
     * @throws ResourceNotFoundException if the review is not found
     */
    @Override
    public void deleteReviewById(Long reviewId) {
        reviewRepository.findById(reviewId).ifPresentOrElse(reviewRepository::delete, () -> {throw new ResourceNotFoundException("Review with product id "+ reviewId);});
    }




}
