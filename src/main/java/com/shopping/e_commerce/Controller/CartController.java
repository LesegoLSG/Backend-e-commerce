package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Cart;
import com.shopping.e_commerce.Services.cart.ICartService;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;
/**
 * CartController handles cart-related endpoints like get cart by id,clear cart by id and getTotal price using id.
 */

@RestController // Marks this class as a REST controller to handle incoming HTTP requests.
@RequiredArgsConstructor // Lombok annotation to generate a constructor for final fields.
@RequestMapping("${api.prefix}/carts") // Base URL mapping for all cart-related endpoints.
public class CartController {

    @Autowired
    private final ICartService cartService;

    /**
     * Endpoint to retrieve a cart by its ID.
     * @param cartId - The ID of the cart to be retrieved.
     * @return ResponseEntity containing an ApiResponse with the cart details or an error message.
     */
    @GetMapping("/{cartId}/getCart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){
        try {
            Cart cart = cartService.getCartById(cartId);
            return ResponseEntity.ok(new ApiResponse("Successfully got the cart", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    /**
     * Endpoint to clear/delete a cart by its ID.
     * @param cartId - The ID of the cart to be cleared.
     * @return ResponseEntity containing an ApiResponse confirming the cart clearance or an error message.
     */
    @DeleteMapping("/{cardId}/delete")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Cart cleared", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Endpoint to get the total price of a cart by its ID.
     * @param cartId - The ID of the cart.
     * @return ResponseEntity containing an ApiResponse with the total price of the cart or an error message.
     */
    @GetMapping("/{cartId}/getTotalPrice")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
