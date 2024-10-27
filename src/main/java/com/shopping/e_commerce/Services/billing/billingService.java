package com.shopping.e_commerce.Services.billing;

import com.shopping.e_commerce.Entity.Billing;
import com.shopping.e_commerce.Entity.ShippingInformation;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.Repository.BillingRepository;
import com.shopping.e_commerce.dto.BillingDTO.BillingDto;
import com.shopping.e_commerce.dto.ShippingInformationDTO.ShippingDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Service class for managing billing information in the e-commerce application.
 * Implements the IBillingService interface to define billing-related operations.
 */

@Service
@RequiredArgsConstructor
public class billingService implements IBillingService{

    private final BillingRepository billingRepository;
    private final ModelMapper modelMapper;

    /**
     * Adds new billing information for a user.
     *
     * @param billingDto the billing data transfer object containing billing details
     * @param user the user associated with the billing information
     * @return the saved Billing entity
     * @throws AuthenticationException if the user is not authenticated
     */
    @Override
    public Billing addBilling(BillingDto billingDto, User user) throws AuthenticationException {
        if(user == null){
            throw new AuthenticationException("Please login again");
        }
        try{
            Billing billing = new Billing();
            billing.setAltNumber(billingDto.getAltNumber());
            billing.setPhoneNumber(billingDto.getPhoneNumber());
            billing.setEmail(billingDto.getEmail());
            billing.setFullName(billingDto.getFullName());
            billing.setUser(user);
            return billingRepository.save(billing);

        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Incorrect data format");
        }
    }

    /**
     * Updates existing billing information based on the provided billing ID.
     *
     * @param billingDto the billing data transfer object containing updated billing details
     * @param billingId the ID of the billing record to be updated
     * @return the updated Billing entity
     * @throws ResourceNotFoundException if the billing information with the given ID does not exist
     */
    @Override
    public Billing updateBilling(BillingDto billingDto, Long billingId) {
        return billingRepository.findById(billingId).map(existingbilling ->{
            existingbilling.setFullName(billingDto.getFullName());
            existingbilling.setEmail(billingDto.getEmail());
            existingbilling.setPhoneNumber(billingDto.getPhoneNumber());
            existingbilling.setAltNumber(billingDto.getAltNumber());
            return billingRepository.save(existingbilling);
        }).orElseThrow(() -> new ResourceNotFoundException("Billing information not found"));

    }
    /**
     * Converts a Billing entity to a Billing DTO.
     *
     * @param billing the Billing entity to convert
     * @return the corresponding Billing DTO
     */
    @Override
    public BillingDto convertToDto(Billing billing) {
        {
            return modelMapper.map(billing, BillingDto.class);
        }
    }
}
