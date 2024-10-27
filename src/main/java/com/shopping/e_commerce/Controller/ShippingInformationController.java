package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.ShippingInformation;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Services.ShippingInformation.IShippingInformationService;
import com.shopping.e_commerce.Services.User.IUserService;
import com.shopping.e_commerce.dto.ShippingInformationDTO.ShippingDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.exceptions.UnauthenticatedUserException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * The ShippingInformationController class handles HTTP requests related to shipping information
 * within the e-commerce application. It provides endpoints for adding, updating, retrieving,
 * and deleting shipping addresses associated with authenticated users.
 *
 * This controller leverages services to manage shipping information and user authentication,
 * ensuring that only authenticated users can perform actions on their shipping addresses.
 * Responses are structured using a standardized ApiResponse format to ensure consistency
 * across the API. Error handling is implemented to manage common exceptions, such as
 * resource not found and data integrity violations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/shippingInfo")
public class ShippingInformationController {

    private final IShippingInformationService iShippingInformationService;
    private final IUserService iUserService;

    /**
     * Adds a new shipping address for the authenticated user.
     *
     * @param shippingDto The data transfer object containing shipping address details.
     * @return A ResponseEntity containing the ApiResponse with a success message and the added shipping address DTO.
     *         If an error occurs (e.g., user not authenticated or data integrity violation),
     *         an appropriate error response is returned.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addShippingInfo(@RequestBody ShippingDto shippingDto){
        System.out.println("get in the body");
        try{

            User user = iUserService.getAuthenticatedUser();

            ShippingInformation shippingInformation = iShippingInformationService.addShippingAddress(shippingDto, user);
            ShippingDto shippingDto1 = iShippingInformationService.convertToDto(shippingInformation);
            return ResponseEntity.ok(new ApiResponse("Shipping address added successfully", shippingDto1));

        }catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }catch (DataIntegrityViolationException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Deletes a shipping address by its ID.
     *
     * @param shippingId The ID of the shipping address to be deleted.
     * @return A ResponseEntity containing the ApiResponse with a success message.
     *         If the shipping address is not found, a not found response is returned.
     */
    @DeleteMapping("/delete/{shippingId}")
    public ResponseEntity<ApiResponse> deleteShippingAddress(@PathVariable Long shippingId){
        try{
            iShippingInformationService.deleteShippingAddress(shippingId);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully", null));

        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * Retrieves a shipping address by its ID.
     *
     * @param shippingId The ID of the shipping address to retrieve.
     * @return A ResponseEntity containing the ApiResponse with the retrieved shipping address.
     *         If the shipping address is not found, a not found response is returned.
     */
    @GetMapping("/getShippingAddress/{shippingId}")
    public ResponseEntity<ApiResponse> getShippingInfoById(@PathVariable Long shippingId){
        try{
            ShippingInformation shippingInformation = iShippingInformationService.getShippingInfoById(shippingId);
            return ResponseEntity.ok(new ApiResponse("Successfully retrieved shipping information", shippingInformation));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Updates an existing shipping address by its ID.
     *
     * @param shippingDto The data transfer object containing updated shipping address details.
     * @param shippingId The ID of the shipping address to be updated.
     * @return A ResponseEntity containing the ApiResponse with a success message and the updated shipping address DTO.
     *         If the shipping address is not found, a not found response is returned.
     */
    @PutMapping("/update/{shippingId}")
    public ResponseEntity<ApiResponse> updateShippingInfo(@RequestBody ShippingDto shippingDto,@PathVariable Long shippingId){
        try{
            ShippingInformation shippingInformation = iShippingInformationService.updateShippingInfo(shippingDto,shippingId);
            ShippingDto convertShippingToDto = iShippingInformationService.convertToDto(shippingInformation);
            return ResponseEntity.ok(new ApiResponse("Updated Successfully", convertShippingToDto));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
