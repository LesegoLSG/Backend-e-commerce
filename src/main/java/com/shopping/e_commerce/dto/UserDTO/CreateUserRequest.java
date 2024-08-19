package com.shopping.e_commerce.dto.UserDTO;

import lombok.Data;
import lombok.Getter;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String contactNo;
    private String gender;
    private String email;
    private String password;
}
