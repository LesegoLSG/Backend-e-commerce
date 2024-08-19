package com.shopping.e_commerce.Services.cart;

import com.shopping.e_commerce.Entity.Cart;
import com.shopping.e_commerce.Entity.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCartById(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    //Initialize new cart
    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
