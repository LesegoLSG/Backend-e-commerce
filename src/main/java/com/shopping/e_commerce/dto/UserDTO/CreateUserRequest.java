package com.shopping.e_commerce.dto.UserDTO;

import com.shopping.e_commerce.Entity.Role;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String contactNo;
    private String email;
    private String password;
    private Collection<Role> roles= new HashSet<>();

}
