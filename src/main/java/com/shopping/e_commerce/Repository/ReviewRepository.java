package com.shopping.e_commerce.Repository;

import com.shopping.e_commerce.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
