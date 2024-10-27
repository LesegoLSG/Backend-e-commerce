package com.shopping.e_commerce.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Entry point for handling unauthorized access attempts
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles unauthorized access attempts by sending a JSON response with an error message.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param authException the exception that triggered the unauthorized access
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet-related error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Set the content type of the response to JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // Set the HTTP status code to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a map to hold the error response body
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "Please login and try again");
        body.put("path", request.getServletPath());
        // Create an ObjectMapper to convert the response body to JSON
        final ObjectMapper mapper = new ObjectMapper();
        // Write the JSON response to the output stream
        mapper.writeValue(response.getOutputStream(), body);

    }
}
