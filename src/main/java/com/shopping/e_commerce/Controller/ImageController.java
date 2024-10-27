package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Image;
import com.shopping.e_commerce.Services.image.IImageService;
import com.shopping.e_commerce.dto.ImageDTO.ImageDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
/**
 * ImageController is a REST controller responsible for handling image-related
 * operations in the shopping e-commerce system. This includes uploading, updating,
 * retrieving, and deleting images associated with products.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    @Autowired
    private final IImageService imageService;

    /**
     * Handles uploading of multiple images for a specific product.
     *
     * @param files List of images to be uploaded (received as multipart files)
     * @param productId The ID of the product to associate the images with
     * @return ResponseEntity containing the uploaded image details or an error message if upload fails
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try{
            List<ImageDto> imageDtos = imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("Uploaded successfully!", imageDtos));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to upload", e.getMessage()));
        }
    }

    /**
     * Downloads an image by its ID.
     *
     * @param id The ID of the image to be downloaded
     * @return ResponseEntity containing the image as a byte stream for download
     * @throws SQLException In case of SQL issues when fetching image data
     */
    @GetMapping("/image/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException{
        Image image = imageService.getImageById(id);
        ByteArrayResource resource =  new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + image.getFileName() + "\"").body(resource);
    }

    /**
     * Updates an existing image by its ID.
     *
     * @param id The ID of the image to be updated
     * @param file The new image file (received as multipart)
     * @return ResponseEntity indicating success or an error message if the image is not found
     */
    @PutMapping("/image/{id}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long id, @RequestBody MultipartFile file){
        try{
            Image image = imageService.getImageById(id);
            if(image != null){
                imageService.updateImage(file,id);
                return ResponseEntity.ok(new ApiResponse("Updated successfully", null));
            }
        }catch(ResourceNotFoundException e){
          return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed", INTERNAL_SERVER_ERROR));
    }

    /**
     * Deletes an image by its ID.
     *
     * @param id The ID of the image to be deleted
     * @return ResponseEntity indicating success or an error message if the image is not found
     */
    @DeleteMapping("/image/{id}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id){
        try{
            Image image = imageService.getImageById(id);
            if(image != null){
                imageService.deleteImageById(id);
                return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));
            }
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed", INTERNAL_SERVER_ERROR));
    }


}
