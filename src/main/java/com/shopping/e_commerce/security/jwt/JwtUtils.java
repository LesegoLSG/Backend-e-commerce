package com.shopping.e_commerce.security.jwt;

import com.shopping.e_commerce.security.user.ShoppingUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Utility class for handling JWT operations
 */
@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}") // Inject JWT secret from properties
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}") // Inject token expiration time
    private int expirationTime;

    @Value("${auth.token.refreshExpirationInMils}") // Inject refresh token expiration time
    private int refreshExpirationTime;


    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication the authentication object containing user details
     * @return a signed JWT token
     */
    public String generateToken(Authentication authentication){
        //Get logged in user
        ShoppingUserDetails userPrincipal = (ShoppingUserDetails) authentication.getPrincipal();

        // Extract user roles
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        // Build and return the JWT token
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + expirationTime)))
                .signWith(key(), SignatureAlgorithm.HS256).compact();

    }

    /**
     * Generates a refresh token for the given user details.
     *
     * @param userDetails the user details
     * @return a signed refresh token
     */
    public String generateRefreshToken(ShoppingUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .claim("id", userDetails.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + refreshExpirationTime))) // Refresh token expiration
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Generates a key for signing JWT tokens using the provided secret.
     *
     * @return the signing key
     */
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts the user ID from the given JWT token.
     *
     * @param token the JWT token
     * @return the user ID extracted from the token
     */
    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class); // Extract the user ID from the token claims
    }

    /**
     * Extracts the username (email) from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUserName(String token){
       return Jwts.parserBuilder()
               .setSigningKey(key())
               .build()
               .parseClaimsJws(token)
               .getBody().getSubject();  // Get the subject (username)
    }


    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token){
        try {
            Jwts.parserBuilder()
                   .setSigningKey(key())
                   .build()
                   .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }

    /**
     * Generates a JWT token using the provided user details.
     *
     * @param userDetails the user details
     * @return a signed JWT token
     */
    public String generateTokenWithUserDetails(ShoppingUserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .claim("id", userDetails.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + expirationTime)))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

}
