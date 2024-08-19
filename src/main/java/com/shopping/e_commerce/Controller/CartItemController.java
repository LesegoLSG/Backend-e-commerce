package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Cart;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Services.User.IUserService;
import com.shopping.e_commerce.Services.cart.ICartItemService;
import com.shopping.e_commerce.Services.cart.ICartService;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    
    @Autowired
    private final ICartItemService cartItemService;

    @Autowired
    private final ICartService cartService;

    @Autowired
    private IUserService userService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,@RequestParam Long productId,@RequestParam Integer quantity){
        try {
            User user = userService.getUserById(1L);
             Cart cart =  cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Added item successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

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
