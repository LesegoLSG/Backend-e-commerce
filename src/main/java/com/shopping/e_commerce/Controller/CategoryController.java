package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Category;
import com.shopping.e_commerce.Services.category.ICategoryService;
import com.shopping.e_commerce.exceptions.AlreadyExistsException;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * CategoryController is a REST controller responsible for managing category-related
 * operations in the shopping e-commerce system. This includes creating, retrieving,
 * updating, and deleting categories. The controller interacts with the category service
 * to perform these operations and handles various HTTP methods such as GET, POST, PUT,
 * and DELETE.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    @Autowired
    private final ICategoryService categoryService;

    /**
     * Retrieves all categories from the system.
     *
     * @return ResponseEntity containing the list of all categories or an error message if an issue occurs.
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try{
            List<Category> categoryList = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found...", categoryList));
        }catch(Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Adds a new category to the system.
     *
     * @param name The category object to be added (in request body)
     * @return ResponseEntity containing the added category or an error message if the category already exists.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name){
        try{
            Category theCategory = categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Successfully added", theCategory));

        }catch(AlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to be retrieved
     * @return ResponseEntity containing the category details or an error message if the category is not found.
     */
    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
      try{
          Category theCategory = categoryService.getCategoryById(id);
          return ResponseEntity.ok(new ApiResponse("Found", theCategory));
      }catch(ResourceNotFoundException e){
          return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
      }

    }

    /**
     * Retrieves a category by its name.
     *
     * @param name The name of the category to be retrieved
     * @return ResponseEntity containing the category details or an error message if the category is not found.
     */
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try{
            Category theCategory = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", theCategory));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to be deleted
     * @return ResponseEntity indicating success or an error message if the category is not found.
     */
    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try{
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found", null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Updates a category by its ID.
     *
     * @param id The ID of the category to be updated
     * @param category The updated category object (in request body)
     * @return ResponseEntity containing the updated category or an error message if the category is not found.
     */
    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id, @RequestBody Category category){
        try{
            Category updatedCategory = categoryService.updateCategory(category,id);
            return ResponseEntity.ok(new ApiResponse("Updated successfully", updatedCategory));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }
}
