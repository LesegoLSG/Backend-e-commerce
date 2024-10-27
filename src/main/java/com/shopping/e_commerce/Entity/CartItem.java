package com.shopping.e_commerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entity class representing an item within a shopping cart.
 * Contains information about quantity, price, associated product, and the cart it belongs to.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    /**
     * The product associated with this cart item.
     * Many CartItems can be associated with a single product.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * The cart to which this item belongs.
     * Configured to cascade operations, ensuring changes in the cart propagate to items.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /**
     * Sets the total price for this cart item based on the unit price and quantity.
     * Should be called whenever the quantity or unit price is updated.
     */
    public void setTotalPrice(){
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }


}
