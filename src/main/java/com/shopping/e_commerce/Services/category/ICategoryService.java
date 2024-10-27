package com.shopping.e_commerce.Services.category;

import com.shopping.e_commerce.Entity.Category;

import java.util.List;
/**
 * Interface defining the operations for managing categories in the e-commerce application.
 */
public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
}
