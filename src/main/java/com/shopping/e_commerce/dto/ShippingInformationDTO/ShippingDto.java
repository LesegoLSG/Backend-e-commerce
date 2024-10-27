package com.shopping.e_commerce.dto.ShippingInformationDTO;

import lombok.Data;

@Data
public class ShippingDto {
    private Long id;
    private String streetAddress;
    private String city;
    private String province;
    private String zipCode;
    private String country;
    private String unit;
}
