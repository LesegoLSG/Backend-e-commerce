package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Cart;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Services.User.IUserService;
import com.shopping.e_commerce.Services.cart.ICartItemService;
import com.shopping.e_commerce.Services.cart.ICartService;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * CartItemController is a REST controller responsible for handling HTTP requests
 * related to the cart items in a shopping cart system. This includes adding items
 * to a user's cart, deleting items from the cart, and updating the quantity of
 * items in the cart. The controller interacts with the user, cart, and cart item
 * services to perform these operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    
    @Autowired
    private final ICartItemService cartItemService;

    @Autowired
    private final ICartService cartService;

    @Autowired
    private final IUserService userService;

    /**
     * Adds an item to the user's cart. The method retrieves the authenticated user,
     * initializes a new cart if one does not exist, and adds the specified item to the cart.
     *
     * @param productId The ID of the product to be added
     * @param quantity The quantity of the product to be added
     * @return ResponseEntity containing the result of the operation (success or error message)
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId,@RequestParam Integer quantity){
        try {
            // Retrieves the authenticated user
            User user = userService.getAuthenticatedUser();

            // Initializes a new cart for the user if one does not exist
             Cart cart =  cartService.initializeNewCart(user);

            // Adds the product to the user's cart
            cartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Added item successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch(JwtException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Deletes an item from the user's cart.
     *
     * @param cartId The ID of the cart from which the item will be deleted
     * @param itemId The ID of the item to be deleted
     * @return ResponseEntity containing the result of the operation (success or error message)
     */
    @DeleteMapping("/cart/{cartId}/item/{itemId}/delete")
    public ResponseEntity<ApiResponse> deleteItemFromCart(@PathVariable Long cartId,@PathVariable Long itemId){
        try {
            System.out.println("Delete data: " + "Cart id="  + cartId + "ItemId=" + itemId);
            cartItemService.deleteItemFromCart(cartId,itemId);
            System.out.println("Delete data2: " + "Cart id="  + cartId + "ItemId=" + itemId);
            return ResponseEntity.ok(new ApiResponse("Deleted item successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Updates the quantity of an item in the user's cart.
     *
     * @param cartId The ID of the cart containing the item
     * @param itemId The ID of the item whose quantity will be updated
     * @param quantity The new quantity of the item
     * @return ResponseEntity containing the result of the operation (success or error message)
     */
    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,@PathVariable Long itemId,@RequestParam Integer quantity){
        try {
            System.out.println("Item controller: " + quantity);
            cartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Updated quantity successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    
    
}
