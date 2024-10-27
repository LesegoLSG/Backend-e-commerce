package com.shopping.e_commerce.Services.ShippingInformation;

import com.shopping.e_commerce.Entity.Order;
import com.shopping.e_commerce.Entity.ShippingInformation;
import com.shopping.e_commerce.Entity.User;
import com.shopping.e_commerce.dto.OrderDTO.OrderDto;
import com.shopping.e_commerce.dto.ShippingInformationDTO.ShippingDto;
/**
 * Interface defining the operations for managing shipping information in the e-commerce application.
 */
public interface IShippingInformationService {
    ShippingInformation addShippingAddress(ShippingDto shippingDto,User user);

    void deleteShippingAddress(Long shippingId);

    ShippingInformation getShippingInfoById(Long shippingId);

    ShippingInformation updateShippingInfo(ShippingDto shippingDto, Long ShippingId);


    public ShippingDto convertToDto(ShippingInformation shippingInformation);

}
