package com.shopping.e_commerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

/**
 * Entity class representing a Role in the e-commerce application.
 * Roles are used to assign different permissions and access levels to users.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    /**
     * Constructor to initialize the Role with a name.
     *
     * @param name the name of the role
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Many-to-Many relationship with User.
     * A role can be assigned to multiple users, and a user can have multiple roles.
     */
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<>();
}
