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

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    @Autowired
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try{
            List<ImageDto> imageDtos = imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("Uploaded successfully!", imageDtos));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to upload", e.getMessage()));
        }
    }
    @GetMapping("/image/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException{
        Image image = imageService.getImageById(id);
        ByteArrayResource resource =  new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + image.getFileName() + "\"").body(resource);
    }

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
