package com.shopping.e_commerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
/**
 * Cart entity representing a user's shopping cart in the e-commerce application.
 * Stores the total amount for the cart, the items within the cart, and a reference to the user who owns the cart.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Set of items in the cart, each represented as a CartItem entity.
     * Configured with cascading operations and orphan removal to manage item persistence.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    /**
     * User associated with this cart, represented by a one-to-one relationship.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Adds a CartItem to the cart, sets its reference to this cart,
     * and updates the total amount to reflect the addition.
     *
     * @param item the CartItem to be added to the cart
     */
    public void addItem(CartItem item){
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    /**
     * Removes a CartItem from the cart, clears its reference to this cart,
     * and updates the total amount to reflect the removal.
     *
     * @param item the CartItem to be removed from the cart
     */
    public void removeItem(CartItem item){
        this.items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    /**
     * Updates the total amount of the cart based on the unit price and quantity of each CartItem.
     * Iterates over each item in the cart and calculates the sum of item prices.
     */
    private void updateTotalAmount(){
        // Calculates total by multiplying unit price by quantity for each item
        this.totalAmount = items.stream().map(item -> {
            BigDecimal unitPrice  = item.getUnitPrice();
            if(unitPrice == null){
                return BigDecimal.ZERO;
            }
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Updated Cart Total 2: " + this.totalAmount);
    }
}
