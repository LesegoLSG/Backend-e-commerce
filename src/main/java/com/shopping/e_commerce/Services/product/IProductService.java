package com.shopping.e_commerce.Services.product;

import com.shopping.e_commerce.Entity.Product;
import com.shopping.e_commerce.dto.ProductDTO.AddProductRequest;
import com.shopping.e_commerce.dto.ProductDTO.ProductDto;
import com.shopping.e_commerce.dto.ProductDTO.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest addRequest);
    Product getProductById(Long productId);
    Product updateProductById(UpdateProductRequest request, Long productId);
    void deleteProductById(Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category,String brand);
    List<Product> getProductByName(String productName);
    List<Product> getProductsByBrandAndName(String category,String name);
    Long countProductByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    public ProductDto convertToDTO(Product product);
}
