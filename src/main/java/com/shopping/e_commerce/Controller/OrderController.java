package com.shopping.e_commerce.Controller;

import com.shopping.e_commerce.Entity.Order;
import com.shopping.e_commerce.Services.Order.IOrderService;
import com.shopping.e_commerce.dto.OrderDTO.OrderDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import com.shopping.e_commerce.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * OrderController is responsible for handling all order-related operations
 * in the e-commerce system, including creating, retrieving by ID, and listing
 * all orders associated with a user.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @Autowired
    private final IOrderService orderService;

    /**
     * Creates an order for a specified user.
     *
     * @param userId ID of the user placing the order
     * @return ResponseEntity containing the created order details or an error message if creation fails
     */
    @PostMapping("/order/add")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Order created successfully", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding an order", e.getMessage()));
        }
    }

    /**
     * Retrieves a specific order by its ID.
     *
     * @param orderId ID of the order to retrieve
     * @return ResponseEntity containing the order details or an error message if not found
     */
    @GetMapping("/{oderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order received successfully", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Sorry ", e.getMessage()));
        }
    }

    /**
     * Retrieves all orders associated with a specific user.
     *
     * @param userId ID of the user whose orders are being retrieved
     * @return ResponseEntity containing the list of user orders or an error message if not found
     */
    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> order = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Orders received successfully", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Sorry ", e.getMessage()));
        }
    }


}
