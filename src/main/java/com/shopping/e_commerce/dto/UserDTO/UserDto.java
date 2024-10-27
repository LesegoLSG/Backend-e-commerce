package com.shopping.e_commerce.dto.UserDTO;

import com.shopping.e_commerce.Entity.Review;
import com.shopping.e_commerce.Entity.ShippingInformation;
import com.shopping.e_commerce.dto.BillingDTO.BillingDto;
import com.shopping.e_commerce.dto.CartDTO.CartDto;
import com.shopping.e_commerce.dto.OrderDTO.OrderDto;
import com.shopping.e_commerce.dto.ReviewDto.ReviewDto;
import com.shopping.e_commerce.dto.ShippingInformationDTO.ShippingDto;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
    private List<ReviewDto> reviews;
    private BillingDto billing;
    private List<ShippingDto> shippingInformations;
}
