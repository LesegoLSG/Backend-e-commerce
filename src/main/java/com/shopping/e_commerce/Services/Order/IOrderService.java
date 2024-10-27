package com.shopping.e_commerce.Services.Order;

import com.shopping.e_commerce.Entity.Order;
import com.shopping.e_commerce.dto.OrderDTO.OrderDto;

import java.util.List;
/**
 * Interface defining the operations for managing orders in the e-commerce application.
 */
public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long useId);

    OrderDto convertToDto(Order order);
}
