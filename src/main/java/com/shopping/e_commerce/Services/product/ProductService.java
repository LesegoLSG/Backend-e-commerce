package com.shopping.e_commerce.Services.product;

import com.shopping.e_commerce.Entity.Category;
import com.shopping.e_commerce.Entity.Image;
import com.shopping.e_commerce.Entity.Product;
import com.shopping.e_commerce.Repository.CategoryRepository;
import com.shopping.e_commerce.Repository.ImageRepository;
import com.shopping.e_commerce.Repository.ProductRepository;
import com.shopping.e_commerce.dto.ImageDTO.ImageDto;
import com.shopping.e_commerce.dto.ProductDTO.AddProductRequest;
import com.shopping.e_commerce.dto.ProductDTO.ProductDto;
import com.shopping.e_commerce.dto.ProductDTO.UpdateProductRequest;
import com.shopping.e_commerce.exceptions.AlreadyExistsException;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing products in the e-commerce application.
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final ImageRepository imageRepository;

    @Autowired
    private final ModelMapper modelMapper;

    /**
     * Adds a new product to the repository.
     *
     * @param request the request containing product details
     * @return the added Product object
     * @throws AlreadyExistsException if a product with the same name and brand already exists
     */
    @Override
    public Product addProduct(AddProductRequest request) {
        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException("Product " + request.getName() + " with brand " + request.getBrand() + " already exists");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }

    /**
     * Adds a new product to the repository.
     *
     * @param request the request containing product details
     * @return the added Product object
     * @throws AlreadyExistsException if a product with the same name and brand already exists
     */
    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                        request.getName(),
                        request.getBrand(),
                        request.getDescription(),
                        request.getCode(),
                        request.getPrice(),
                        request.getInventory(),
                        category
                );
    }
    /**
     * Retrieves a product by its ID.
     *
     * @param productId the ID of the product
     * @return the corresponding Product object
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    /**
     * Updates a product by its ID.
     *
     * @param request the request containing updated product details
     * @param productId the ID of the product to update
     * @return the updated Product object
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public Product updateProductById(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    /**
     * Updates an existing Product entity with new details from the request.
     *
     * @param existingProduct the existing Product to update
     * @param request the UpdateProductRequest containing updated details
     * @return the updated Product object
     */
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setCode(request.getCode());
        existingProduct.setDescription(request.getDescription());
         existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    /**
     * Deletes a product by its ID.
     *
     * @param productId the ID of the product to delete
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId).ifPresentOrElse(productRepository::delete, () -> {throw new ResourceNotFoundException("Product not found");});
    }


    /**
     * Retrieves all products from the repository.
     *
     * @return a list of all Product objects
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves products by category name.
     *
     * @param category the name of the category
     * @return a list of Product objects belonging to the specified category
     */
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    /**
     * Retrieves products by brand name.
     *
     * @param brand the name of the brand
     * @return a list of Product objects belonging to the specified brand
     */
    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    /**
     * Retrieves products by category and brand name.
     *
     * @param category the name of the category
     * @param brand the name of the brand
     * @return a list of Product objects belonging to the specified category and brand
     */
    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    /**
     * Retrieves products by name.
     *
     * @param productName the name of the product
     * @return a list of Product objects matching the specified name
     */
    @Override
    public List<Product> getProductByName(String productName) {
        return productRepository.findByName(productName);
    }

    /**
     * Retrieves products by brand and name.
     *
     * @param brand the name of the brand
     * @param name the name of the product
     * @return a list of Product objects matching the specified brand and name
     */
    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    /**
     * Counts the number of products by brand and name.
     *
     * @param brand the name of the brand
     * @param name the name of the product
     * @return the count of products matching the specified brand and name
     */
    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    /**
     * Converts a list of Product entities to ProductDto objects.
     *
     * @param products the list of Product entities to convert
     * @return a list of converted ProductDto objects
     */
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDTO).toList();
    }
    /**
     * Converts a Product entity to a ProductDto.
     *
     * @param product the Product to convert
     * @return the converted ProductDto
     */
    @Override
    public ProductDto convertToDTO(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imagesDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imagesDtos);
        return productDto;

    }
}
