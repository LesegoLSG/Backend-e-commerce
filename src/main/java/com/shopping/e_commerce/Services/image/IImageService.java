package com.shopping.e_commerce.Services.image;

import com.shopping.e_commerce.Entity.Image;
import com.shopping.e_commerce.dto.ImageDTO.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);

}
