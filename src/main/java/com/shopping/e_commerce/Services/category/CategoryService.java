package com.shopping.e_commerce.Services.category;

import com.shopping.e_commerce.Entity.Category;
import com.shopping.e_commerce.Repository.CategoryRepository;
import com.shopping.e_commerce.exceptions.AlreadyExistsException;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    @Autowired
    private final CategoryRepository categoryRepository;




    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(() -> new AlreadyExistsException(category.getName() + "Already Exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
       return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
           oldCategory.setName(category.getName());
           return categoryRepository.save(oldCategory);
       }).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,() -> {
            throw new ResourceNotFoundException("Category Not found.");
        });
    }
}
