package com.shopping.e_commerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Billing entity representing the billing information for a user in the e-commerce application.
 * It stores details such as the user's full name, contact information, and is associated with a User entity.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String altNumber;

    /**
     * User entity associated with this billing record, mapped by a one-to-one relationship.
     * Indicates the user who owns this billing information.
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
