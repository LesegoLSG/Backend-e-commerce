package com.shopping.e_commerce.Services.image;

import com.shopping.e_commerce.Entity.Image;
import com.shopping.e_commerce.Entity.Product;
import com.shopping.e_commerce.Repository.ImageRepository;
import com.shopping.e_commerce.Repository.ProductRepository;
import com.shopping.e_commerce.Services.product.ProductService;
import com.shopping.e_commerce.dto.ImageDTO.ImageDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing images associated with products.
 */

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    @Autowired
    private final ImageRepository imageRepository;

    @Autowired
    private final ProductService productService;

    /**
     * Retrieves an image by its ID.
     *
     * @param id the ID of the image
     * @return the Image object associated with the given ID
     * @throws ResourceNotFoundException if the image does not exist
     */
    @Override
    public Image getImageById(Long id) {
       return imageRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
    }

    /**
     * Deletes an image by its ID.
     *
     * @param id the ID of the image to delete
     * @throws ResourceNotFoundException if the image does not exist
     */
    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository :: delete, () ->{
            throw  new ResourceNotFoundException("Error deleting an image with id:" + id);
        });
    }

    /**
     * Saves multiple images associated with a product.
     *
     * @param files the list of MultipartFile images to save
     * @param productId the ID of the product associated with the images
     * @return a list of ImageDto objects representing the saved images
     */
    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
     Product product = productService.getProductById(productId);

     List<ImageDto> savedImageDto = new ArrayList<>();
     for(MultipartFile file: files){
         try{
             Image image = new Image();
             image.setFileName(file.getOriginalFilename());
             image.setFileType(file.getContentType());
             image.setImage(new SerialBlob(file.getBytes()));
             image.setProduct(product);

             String buildDownloadUrl = "/api/v1/images/image/download/";

             String downloadUrl = buildDownloadUrl + image.getId();
             image.setDownloadUrl(downloadUrl);
             Image savedImage = imageRepository.save(image);
             savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId() );
             imageRepository.save(savedImage);

             ImageDto imageDto = new ImageDto();
             imageDto.setId(savedImage.getId());
             imageDto.setFileName(savedImage.getFileName());
             imageDto.setDownloadUrl(savedImage.getDownloadUrl());
             savedImageDto.add(imageDto);

         }catch(IOException  | SQLException e){
                throw  new RuntimeException(e.getMessage());
         }
     }
        return savedImageDto;
    }
    /**
     * Updates an existing image.
     *
     * @param file the new MultipartFile image to replace the existing one
     * @param imageId the ID of the image to update
     * @throws RuntimeException if an error occurs while updating the image
     */
    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);

        try{
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch(IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
