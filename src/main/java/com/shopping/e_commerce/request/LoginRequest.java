package com.shopping.e_commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * LoginRequest class to handle login requests
 */
@Data
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
