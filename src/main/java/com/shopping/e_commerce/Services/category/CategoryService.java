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

/**
 * Service class for managing categories in the e-commerce application.
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    @Autowired
    private final CategoryRepository categoryRepository;

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return the Category object associated with the given ID
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }

    /**
     * Retrieves a category by its name.
     *
     * @param name the name of the category
     * @return the Category object associated with the given name
     */
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all Category objects
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Adds a new category if it does not already exist.
     *
     * @param category the Category object to add
     * @return the added Category object
     * @throws AlreadyExistsException if the category with the same name already exists
     */
    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save).orElseThrow(() -> new AlreadyExistsException(category.getName() + "Already Exists"));
    }

    /**
     * Updates an existing category by its ID.
     *
     * @param category the updated Category object
     * @param id the ID of the category to update
     * @return the updated Category object
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public Category updateCategory(Category category, Long id) {
       return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
           oldCategory.setName(category.getName());
           return categoryRepository.save(oldCategory);
       }).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,() -> {
            throw new ResourceNotFoundException("Category Not found.");
        });
    }
}
