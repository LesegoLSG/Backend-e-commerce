package com.shopping.e_commerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ApiResponse class to structure the response from the API
 */
@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Object data;
}
