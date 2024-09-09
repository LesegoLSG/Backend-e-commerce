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

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                        request.getName(),
                        request.getBrand(),
                        request.getDescription(),
                        request.getPrice(),
                        request.getInventory(),
                        category
                );
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product updateProductById(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
         existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId).ifPresentOrElse(productRepository::delete, () -> {throw new ResourceNotFoundException("Product not found");});
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDTO).toList();
    }

    @Override
    public ProductDto convertToDTO(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imagesDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imagesDtos);
        return productDto;

    }
}
