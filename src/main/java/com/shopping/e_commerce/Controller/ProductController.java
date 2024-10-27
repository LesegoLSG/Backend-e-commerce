package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Product;
import com.shopping.e_commerce.Services.product.IProductService;
import com.shopping.e_commerce.dto.ProductDTO.AddProductRequest;
import com.shopping.e_commerce.dto.ProductDTO.ProductDto;
import com.shopping.e_commerce.dto.ProductDTO.UpdateProductRequest;
import com.shopping.e_commerce.exceptions.AlreadyExistsException;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * The ProductController class handles HTTP requests related to product management in the e-commerce application.
 * It provides endpoints to perform CRUD operations on products, including retrieving all products,
 * adding a new product, updating an existing product, and deleting a product by ID.
 * Additionally, it includes methods to fetch products based on specific criteria such as brand, category,
 * and name, along with functionalities to count products by various attributes.
 *
 * This controller interacts with the product service layer to ensure separation of concerns,
 * maintaining clean architecture principles. Each method is annotated with appropriate HTTP method
 * annotations, and responses are encapsulated in a standardized ApiResponse format for consistency.
 */

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    /**
     * Retrieves all products in the store.
     *
     * @return ResponseEntity containing a list of all products
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Successfully retrieved all products", convertedProducts));
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product
     * @return ResponseEntity containing the product details or an error message
     */
    @GetMapping("product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try{
            Product product = productService.getProductById(id);
            var productDto = productService.convertToDTO(product);
            return ResponseEntity.ok(new ApiResponse("Success", productDto));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    /**
     * Adds a new product to the store.
     *
     * @param product The product details to add
     * @return ResponseEntity indicating the result of the add operation
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            Product theProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Added product successfully", theProduct));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param request The updated product details
     * @param id      The ID of the product to update
     * @return ResponseEntity indicating the result of the update operation
     */
    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long id){
        try{
            Product product = productService.updateProductById(request,id);
            return ResponseEntity.ok(new ApiResponse("Updated successfully", product));

        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        try{
         productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Updated successfully", id));

        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Retrieves products by brand and name.
     *
     * @param brandName   The name of the brand
     * @param productName The name of the product
     * @return ResponseEntity containing the found products or an error message
     */
    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brandName, @RequestParam String productName){
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName,productName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Retrieves products by category and brand.
     *
     * @param category The category of the products
     * @param brand    The brand of the products
     * @return ResponseEntity containing the found products or an error message
     */
    @GetMapping("/products/by/category-and-name")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category,brand);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Retrieves products by name.
     *
     * @param name The name of the product
     * @return ResponseEntity containing the found products or an error message
     */
    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@PathVariable String name){
        try {
            List<Product> products = productService.getProductByName(name);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Retrieves products by brand.
     *
     * @param brand The brand of the products
     * @return ResponseEntity containing the found products or an error message
     */
    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByBrand(brand);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Retrieves products by category.
     *
     * @param category The category of the products
     * @return ResponseEntity containing the found products or an error message
     */
    @GetMapping("/products/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category){
        try {
            List<Product> products = productService.getProductsByCategory(category);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Products found", null));
            }
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }


    /**
     * Counts products by brand and name.
     *
     * @param brand The brand of the products
     * @param name  The name of the products
     * @return ResponseEntity containing the product count
     */
    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try {
            var productCounter = productService.countProductByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Success", productCounter));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}
