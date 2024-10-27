package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Review;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Services.User.IUserService;
import com.shopping.e_commerce.Services.review.IReviewService;
import com.shopping.e_commerce.dto.ReviewDto.ReviewDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * The ReviewController class handles HTTP requests related to product reviews within the e-commerce application.
 * It provides endpoints for adding and deleting reviews for products, allowing users to share their feedback
 * on products they have purchased. The controller ensures that reviews are linked to authenticated users,
 * promoting accountability and trust in user feedback.
 *
 * This controller utilizes the review service to manage review data and the user service to authenticate
 * users. Responses are structured using a standardized ApiResponse format, ensuring consistency across
 * the API. Error handling is implemented to manage common exceptions, including resource not found scenarios.
 */
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/review")
public class ReviewController {


    private final IReviewService iReviewService;


    private final IUserService iUserService;

    /**
     * Adds a review for a specific product.
     *
     * @param reviewDto The review data transfer object containing the review details.
     * @param productId The ID of the product for which the review is being added.
     * @return A ResponseEntity containing the ApiResponse with a success message and the added review.
     *         If an error occurs during the process, a bad request response is returned.
     */
    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResponse> addReviewToProduct(@RequestBody ReviewDto reviewDto, @PathVariable Long productId){
        try{
            User user = iUserService.getAuthenticatedUser();
            if (user == null) {
                throw new RuntimeException("Authenticated user is null");
            }
            System.out.println("Authenticated user: " + user.getEmail());

            Review review = iReviewService.addReviewToProduct(reviewDto,productId, user);
            System.out.println("Does it gets here");
            return ResponseEntity.ok(new ApiResponse("Thank you, your review is added successfully", review));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse("Error adding a review", null));
        }
    }

    /**
     * Deletes a review by its ID.
     *
     * @param reviewId The ID of the review to be deleted.
     * @return A ResponseEntity containing the ApiResponse with a success message and the deleted review ID.
     *         If the review is not found, a not found response is returned.
     */
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId){
        try{
           iReviewService.deleteReviewById(reviewId);
            return ResponseEntity.ok(new ApiResponse("Removed successfully", reviewId));

        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
