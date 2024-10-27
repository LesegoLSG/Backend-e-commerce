package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Billing;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Services.User.IUserService;
import com.shopping.e_commerce.Services.billing.IBillingService;
import com.shopping.e_commerce.dto.BillingDTO.BillingDto;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * BillingController handles billing-related endpoints like adding and updating billing.
 */

@RestController // Marks this class as a REST controller to handle HTTP requests.
@RequiredArgsConstructor // Lombok annotation to automatically generate a constructor with required fields.
@RequestMapping("${api.prefix}/billing") // Base URL mapping for all endpoints in this controller.
public class BillingController {

    @Autowired
    private final IBillingService iBillingService;
    private final IUserService iUserService;

    /**
     * Endpoint to add billing information for a user.
     * @param billingDto - the billing details passed in the request body.
     * @return ResponseEntity with an ApiResponse containing the added billing information or an error message.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBillingInfo(@RequestBody BillingDto billingDto){
        try{
            User user = iUserService.getAuthenticatedUser();

            Billing billing = iBillingService.addBilling(billingDto,user);
            BillingDto billingDto1 = iBillingService.convertToDto(billing);
            return ResponseEntity.ok(new ApiResponse("Successfully added billing information", billingDto1));

        }catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Endpoint to update existing billing information.
     * @param billingDto - the updated billing details passed in the request body.
     * @param billingId - the ID of the billing record to update.
     * @return ResponseEntity with an ApiResponse containing the updated billing information or an error message.
     */
    @PutMapping("/update/{billingId}")
    public ResponseEntity<ApiResponse> updateBillingInfo(@RequestBody BillingDto billingDto, @PathVariable Long billingId){
        try{
            Billing billing = iBillingService.updateBilling(billingDto,billingId);
            BillingDto billingDto1 = iBillingService.convertToDto(billing);
            return ResponseEntity.ok(new ApiResponse("Successfully updated", billingDto1));
        }catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
