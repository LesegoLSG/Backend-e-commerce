package com.shopping.e_commerce.Repository;

import com.shopping.e_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}