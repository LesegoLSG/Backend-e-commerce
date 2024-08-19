package com.shopping.e_commerce.Services.cart;

import com.shopping.e_commerce.Entity.Cart;
import com.shopping.e_commerce.Entity.CartItem;
import com.shopping.e_commerce.Entity.Product;
import com.shopping.e_commerce.Repository.CartItemRepository;
import com.shopping.e_commerce.Repository.CartRepository;
import com.shopping.e_commerce.Services.product.IProductService;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    @Autowired
    private final CartItemRepository cartItemRepository;
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final IProductService productService;
    @Autowired
    private final ICartService cartService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //Get the cart
        Cart cart = cartService.getCartById(cartId);
        //Get the product
        Product product = productService.getProductById(productId);
        //Check if the product already in the cart
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        //if yes then increase the quantity with the requested quantity
        //if no then initiate a new cart item entry
        if(cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void deleteItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartService.getCartById(cartId);
        CartItem itemToDelete = getCartItem(cartId,itemId);
        System.out.println("item to delete:" + itemToDelete.getProduct().getName());
        cart.removeItem(itemToDelete);
        cartRepository.save(cart);
    }
    //Does not update the cart totalprice
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCartById(cartId);
        System.out.println("CartId: " + cartId + " productId: " + productId + " quantity: " + quantity + " generated cart selection total amount:" + cart.getTotalAmount());

        cart.getItems().forEach(item -> {
            System.out.println("---CartItem ID: " + item.getId() + ", Product ID: " + item.getProduct().getId());
        });

        cart.getItems()
                .stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                    System.out.println("Service: " + item.getUnitPrice());
                });
        BigDecimal totalAmount = cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);

    }

    @Override
    public CartItem getCartItem(Long cartId, Long itemId){
        Cart cart = cartService.getCartById(cartId);
        System.out.println("Get cart details:" + cart.getId() + " price:" + cart.getTotalAmount());
        return cart.getItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
