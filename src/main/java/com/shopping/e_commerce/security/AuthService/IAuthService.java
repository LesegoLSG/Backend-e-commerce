package com.shopping.e_commerce.security.AuthService;

import com.shopping.e_commerce.request.RefreshTokenRequest;
import com.shopping.e_commerce.response.JwtResponse;
/**
 * Interface defining the operations for managing authentication in the e-commerce application.
 */
public interface IAuthService {

    JwtResponse refreshToken(RefreshTokenRequest refreshToken);
}
