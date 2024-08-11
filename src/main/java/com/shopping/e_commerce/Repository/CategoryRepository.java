package com.shopping.e_commerce.Repository;

import com.shopping.e_commerce.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String Name);

    boolean existsByName(String name);
}
