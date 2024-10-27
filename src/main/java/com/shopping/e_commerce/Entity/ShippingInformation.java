package com.shopping.e_commerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entity class representing the shipping information for a user in the e-commerce application.
 * This information is used to process orders and facilitate shipping.
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ShippingInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String streetAddress;
    private String city;
    private String province;
    private String zipCode;
    private String country;
    private String unit;

    /**
     * Many-to-One relationship with User.
     * Each shipping information entry is associated with a single user.
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;


}
