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
/**
 * Service class for managing cart items in the e-commerce application.
 * Implements the ICartItemService interface to define cart item-related operations.
 */

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

    /**
     * Adds an item to the cart. If the item already exists, its quantity is updated.
     *
     * @param cartId the ID of the cart
     * @param productId the ID of the product to add
     * @param quantity the quantity of the product to add
     */
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

    /**
     * Deletes an item from the cart.
     *
     * @param cartId the ID of the cart
     * @param itemId the ID of the cart item to delete
     */
    @Override
    public void deleteItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartService.getCartById(cartId);
        CartItem itemToDelete = getCartItem(cartId,itemId);
        System.out.println("item to delete:" + itemToDelete.getProduct().getName());
        cart.removeItem(itemToDelete);
        cartRepository.save(cart);
    }
    /**
     * Updates the quantity of an item in the cart.
     * This method does not update the total price of the cart.
     *
     * @param cartId the ID of the cart
     * @param productId the ID of the product to update
     * @param quantity the new quantity of the product
     */
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        // Get the cart by ID
        Cart cart = cartService.getCartById(cartId);
        System.out.println("CartId: " + cartId + " productId: " + productId + " quantity: " + quantity + " generated cart selection total amount:" + cart.getTotalAmount());

        // Print details of items in the cart for debugging
        cart.getItems().forEach(item -> {
            System.out.println("---CartItem ID: " + item.getId() + ", Product ID: " + item.getProduct().getId());
        });

        // Update the quantity of the specified cart item
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
        // Recalculate total amount for the cart
        BigDecimal totalAmount = cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        // Save the updated cart to the database
        cartRepository.save(cart);

    }

    /**
     * Retrieves a cart item by cart ID and item ID.
     *
     * @param cartId the ID of the cart
     * @param itemId the ID of the cart item
     * @return the CartItem object
     * @throws ResourceNotFoundException if the item is not found
     */
    @Override
    public CartItem getCartItem(Long cartId, Long itemId){
        // Get the cart by ID
        Cart cart = cartService.getCartById(cartId);

        // Find the cart item in the cart
        System.out.println("Get cart details:" + cart.getId() + " price:" + cart.getTotalAmount());
        return cart.getItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
