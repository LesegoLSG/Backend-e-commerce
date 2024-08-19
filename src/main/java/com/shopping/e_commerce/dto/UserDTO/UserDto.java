package com.shopping.e_commerce.dto.UserDTO;

import com.shopping.e_commerce.dto.CartDTO.CartDto;
import com.shopping.e_commerce.dto.OrderDTO.OrderDto;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String gender;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
