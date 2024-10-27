package com.shopping.e_commerce.security.AuthService;

import com.shopping.e_commerce.request.RefreshTokenRequest;
import com.shopping.e_commerce.response.JwtResponse;
import com.shopping.e_commerce.security.jwt.JwtUtils;
import com.shopping.e_commerce.security.user.ShoppingUserDetails;
import com.shopping.e_commerce.security.user.ShoppingUserDetailsService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing authentication associated with users details.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{

    private final JwtUtils jwtUtils;

    private final ShoppingUserDetailsService shoppingUserDetailsService;

    /**
     * Refreshes the JWT access and refresh tokens using the provided refresh token.
     *
     * @param request The request object containing the refresh token to be validated.
     * @return JwtResponse containing the user's ID, new access token, and new refresh token.
     * @throws JwtException if the refresh token is invalid.
     */
    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if(jwtUtils.isTokenValid(refreshToken)){
            String email = jwtUtils.extractUserName(refreshToken);

            ShoppingUserDetails userDetails = (ShoppingUserDetails) shoppingUserDetailsService.loadUserByUsername(email);

            //generate access and refresh token
            String newAccessToken = jwtUtils.generateTokenWithUserDetails(userDetails);
            String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);
            return new JwtResponse(userDetails.getId(), newAccessToken,newRefreshToken);


        }
        throw new JwtException("Invalid refresh token");
    }
}
