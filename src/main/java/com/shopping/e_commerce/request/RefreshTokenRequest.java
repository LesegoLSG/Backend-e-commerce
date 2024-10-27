package com.shopping.e_commerce.request;

import lombok.Data;
// RefreshTokenRequest class to handle refresh token requests
@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
