package com.shopping.e_commerce.Services.billing;

import com.shopping.e_commerce.Entity.Billing;
import com.shopping.e_commerce.Entity.ShippingInformation;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.dto.BillingDTO.BillingDto;
import com.shopping.e_commerce.dto.ShippingInformationDTO.ShippingDto;
import org.apache.tomcat.websocket.AuthenticationException;
/**
 * Interface defining the operations for managing billing in the e-commerce application.
 */
public interface IBillingService {
    Billing addBilling(BillingDto billingDto, User user) throws AuthenticationException;

    Billing updateBilling(BillingDto billingDto, Long billingId);
    public BillingDto convertToDto(Billing billing);

}
