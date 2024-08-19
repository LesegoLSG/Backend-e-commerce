package com.shopping.e_commerce.Repository;

import com.shopping.e_commerce.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {


    List<Image> findByProductId(Long id);
}
