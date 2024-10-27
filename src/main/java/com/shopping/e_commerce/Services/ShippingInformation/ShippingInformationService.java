package com.shopping.e_commerce.Services.ShippingInformation;

import com.shopping.e_commerce.Entity.ShippingInformation;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Repository.ShippingInformationRepository;
import com.shopping.e_commerce.Repository.UserRepository;
import com.shopping.e_commerce.dto.OrderDTO.OrderDto;
import com.shopping.e_commerce.dto.ShippingInformationDTO.ShippingDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.exceptions.UnauthenticatedUserException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.io.IOException;
/**
 * Service class for managing shipping information associated with user.
 */
@RequiredArgsConstructor
@Service
public class ShippingInformationService  implements IShippingInformationService{

    private final ShippingInformationRepository shippingInformationRepository;

    private final ModelMapper modelMapper;

    /**
     * Adds a new shipping address for the specified user.
     *
     * @param shippingDto the DTO containing shipping address details
     * @param user the user for whom the shipping address is being added
     * @return the saved ShippingInformation object
     * @throws DataIntegrityViolationException if the shipping address violates data integrity constraints
     */
    @Override
    public ShippingInformation addShippingAddress(ShippingDto shippingDto,User user) {
        try{
            ShippingInformation shippingInformation = new ShippingInformation();
            shippingInformation.setStreetAddress(shippingDto.getStreetAddress());
            shippingInformation.setCity(shippingDto.getCity());
            shippingInformation.setProvince(shippingDto.getProvince());
            shippingInformation.setZipCode(shippingDto.getZipCode());
            shippingInformation.setCountry(shippingDto.getCountry());
            shippingInformation.setUnit(shippingDto.getUnit());
            shippingInformation.setUser(user);
            return shippingInformationRepository.save(shippingInformation);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Shipping address violates data integrity constraints.");

        }

    }
    /**
     * Deletes a shipping address by its ID.
     *
     * @param shippingId the ID of the shipping address to delete
     * @throws ResourceNotFoundException if the shipping address is not found
     */
    @Override
    public void deleteShippingAddress(Long shippingId) {
        shippingInformationRepository.findById(shippingId).ifPresentOrElse(shippingInformationRepository::delete, () -> {throw new ResourceNotFoundException("Shipping address with product id "+ shippingId);});
    }

    /**
     * Retrieves a shipping information object by its ID.
     *
     * @param shippingId the ID of the shipping information to retrieve
     * @return the ShippingInformation object
     * @throws ResourceNotFoundException if the shipping information is not found
     */
    @Override
    public ShippingInformation getShippingInfoById(Long shippingId) {
        return shippingInformationRepository.findById(shippingId).orElseThrow(() -> new ResourceNotFoundException("Shipping information not found"));

    }

    /**
     * Updates an existing shipping information entry.
     *
     * @param shippingDto the DTO containing updated shipping address details
     * @param shippingId  the ID of the shipping information to update
     * @return the updated ShippingInformation object
     * @throws ResourceNotFoundException if the shipping information is not found
     */
    @Override
    public ShippingInformation updateShippingInfo(ShippingDto shippingDto, Long shippingId) {
        return shippingInformationRepository.findById(shippingId).map(existingInfo -> {
            existingInfo.setStreetAddress(shippingDto.getStreetAddress());
            existingInfo.setCity(shippingDto.getCity());
            existingInfo.setProvince(shippingDto.getProvince());
            existingInfo.setZipCode(shippingDto.getZipCode());
            existingInfo.setCountry(shippingDto.getCountry());
            existingInfo.setUnit(shippingDto.getUnit());
            return shippingInformationRepository.save(existingInfo);
        }).orElseThrow(() -> new ResourceNotFoundException("Shipping Information not found"));
    }

    /**
     * Converts a ShippingInformation object to a ShippingDto object.
     *
     * @param shippingInformation the ShippingInformation object to convert
     * @return the converted ShippingDto object
     */
    @Override
    public ShippingDto convertToDto(ShippingInformation shippingInformation) {
        {
            return modelMapper.map(shippingInformation, ShippingDto.class);
        }
    }
}
