package com.shopping.e_commerce.Services.cart;

import com.shopping.e_commerce.Entity.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void deleteItemFromCart(Long cartId, Long itemId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
