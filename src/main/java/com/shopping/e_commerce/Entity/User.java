package com.shopping.e_commerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Entity class representing a user in the e-commerce application.
 * This class holds user details and their relationships with other entities such as Cart, Orders, Reviews, etc.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String contactNo;
    @NaturalId
    private String email;
    private String password;

    /**
     * One-to-One relationship with Cart.
     * Each user has one cart associated with them.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    /**
     * One-to-Many relationship with Order.
     * A user can have multiple orders.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> order;

    /**
     * One-to-Many relationship with Review.
     * A user can leave multiple reviews for products.
     */
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews;

    /**
     * One-to-Many relationship with ShippingInformation.
     * A user can have multiple shipping addresses.
     */
    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL,orphanRemoval = true)
    private List<ShippingInformation> shippingInformations;

    /**
     * One-to-One relationship with Billing.
     * Each user has one billing information associated with them.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Billing billing;

    /**
     * Many-to-Many relationship with Role.
     * A user can have multiple roles, and roles can be assigned to multiple users.
     */
    @ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name= "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles= new HashSet<>();
}
