package com.shopping.e_commerce.security.jwt;

import com.shopping.e_commerce.security.user.ShoppingUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for processing JWT authentication in incoming requests.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // JWT utility class for handling token operations
    @Autowired
    private JwtUtils jwtUtils;

    // Service for loading user details based on username
    @Autowired
    private ShoppingUserDetailsService userDetailsService;

    /**
     * Processes the incoming request to authenticate the user based on the JWT token.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue the request processing
     * @throws ServletException if an error occurs during servlet processing
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull  HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);  // Extract JWT from the request

            // If JWT is present and valid, authenticate the user
            if(StringUtils.hasText(jwt) && jwtUtils.isTokenValid(jwt)){
                String username = jwtUtils.extractUserName(jwt); // Extract username from the JWT
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Load user details

                // Create authentication token and set it in the security context
                var auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            // Handle JWT-related exceptions
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() + " Incorrect or expired token, you may login again and try again.");
            return;
        }catch(Exception e){
            // Handle other exceptions
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;
        }
        filterChain.doFilter(request,response);
    }

    /**
     * Parses the JWT from the Authorization header in the request.
     *
     * @param request the HTTP request
     * @return the extracted JWT or null if not found
     */
    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        return null;
    }
}
