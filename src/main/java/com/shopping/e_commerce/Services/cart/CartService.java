package com.shopping.e_commerce.Services.cart;

import com.shopping.e_commerce.Entity.Cart;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Repository.CartItemRepository;
import com.shopping.e_commerce.Repository.CartRepository;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service class for managing cart operations in the e-commerce application.
 * Implements the ICartService interface to define cart-related functionalities.
 */
@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final CartItemRepository cartItemRepository;

    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    /**
     * Retrieves a cart by its ID. Throws an exception if not found.
     *
     * @param id the ID of the cart
     * @return the Cart object
     * @throws ResourceNotFoundException if the cart is not found
     */
    @Override
    public Cart getCartById(Long id) {
        // Find the cart by ID, or throw an exception if not found
        Cart cart  = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        // Ensure the total amount is updated and return the cart
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    /**
     * Clears all items from the cart and deletes the cart from the database.
     *
     * @param id the ID of the cart to clear
     */
    @Transactional
    @Override
    public void clearCart(Long id) {
        // Get the cart by ID
        Cart cart = getCartById(id);
        // Delete all items associated with the cart
        cartItemRepository.deleteAllByCartId(id);
        // Clear the cart's items and delete the cart
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    /**
     * Retrieves the total price of the cart.
     *
     * @param id the ID of the cart
     * @return the total price of the cart
     */
    @Override
    public BigDecimal getTotalPrice(Long id) {
        // Get the cart and return its total amount
        Cart cart = getCartById(id);
        return cart.getTotalAmount();
    }

    /**
     * Initializes a new cart for the user if they do not have one.
     *
     * @param user the User for whom to initialize the cart
     * @return the initialized Cart object
     */
    @Override
    public Cart initializeNewCart(User user){
        // Get the cart associated with the user, or create a new one if none exists
      return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(() -> {
          Cart cart = new Cart();
          cart.setUser(user);
          return cartRepository.save(cart);
      });
    }

    /**
     * Retrieves the cart associated with a user by their user ID.
     *
     * @param userId the ID of the user
     * @return the Cart object associated with the user
     */
    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
