package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.request.LoginRequest;
import com.shopping.e_commerce.request.RefreshTokenRequest;
import com.shopping.e_commerce.response.ApiResponse;
import com.shopping.e_commerce.response.JwtResponse;
import com.shopping.e_commerce.security.AuthService.IAuthService;
import com.shopping.e_commerce.security.jwt.JwtUtils;
import com.shopping.e_commerce.security.user.ShoppingUserDetails;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
/**
 * AuthController handles authentication-related endpoints like login and token refreshing.
 */
@CrossOrigin // Allows cross-origin requests (used to enable frontend requests from different domains)
@RestController // Marks this class as a REST controller where methods return data rather than views
@RequiredArgsConstructor // Automatically injects final fields through constructor
@RequestMapping("${api.prefix}/auth") // Base path for authentication-related endpoints, configurable via properties
public class AuthController {
    private final AuthenticationManager authenticationManager; // Manages authentication for user credentials
    private final JwtUtils jwtUtils; // Utility class for handling JWT (token generation and validation)
    private final IAuthService iAuthService; // Service interface to handle authentication-related logic

    /**
     * Login endpoint that authenticates the user and generates JWT access and refresh tokens.
     *
     * @param loginRequest The login request payload containing email and password.
     * @return ResponseEntity with ApiResponse, containing JWT access and refresh tokens if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        try{
            // Authenticates the user with email and password
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

            // Sets the authenticated user in the Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generates JWT access token for the authenticated user
            String jwt = jwtUtils.generateToken(authentication);

            // Retrieves user details from the authenticated principal
            ShoppingUserDetails userDetails = (ShoppingUserDetails) authentication.getPrincipal();

            // Generates a refresh token for the user
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(),jwt,refreshToken);
            return ResponseEntity.ok(new ApiResponse("Login Successfully",jwtResponse));
        }catch(AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }

    /**
     * Endpoint to refresh the JWT token using a refresh token.
     *
     * @param request The refresh token request payload.
     * @return ResponseEntity with ApiResponse, containing new access and refresh tokens if successful.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequest request){
        try{
            JwtResponse newTokens = iAuthService.refreshToken(request);
            return ResponseEntity.ok(new ApiResponse("Token refreshed successfully", newTokens));
        }catch(JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
