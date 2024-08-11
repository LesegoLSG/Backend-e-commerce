package com.shopping.e_commerce.Repository;

import com.shopping.e_commerce.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ImageRepository extends JpaRepository<Image, Long> {


    List<Image> findByProductId(Long id);
}
