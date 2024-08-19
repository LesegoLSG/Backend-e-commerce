package com.shopping.e_commerce.Services.Order;

import com.shopping.e_commerce.Entity.Cart;
import com.shopping.e_commerce.Entity.Order;
import com.shopping.e_commerce.Entity.OrderItem;
import com.shopping.e_commerce.Entity.Product;
import com.shopping.e_commerce.Enums.OrderStatus;
import com.shopping.e_commerce.Repository.OrderRepository;
import com.shopping.e_commerce.Repository.ProductRepository;
import com.shopping.e_commerce.Services.cart.ICartService;
import com.shopping.e_commerce.dto.OrderDTO.OrderDto;
import com.shopping.e_commerce.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ICartService cartService;
    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calcTotalPrice(orderItemList));
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return savedOrder;
    }



    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }


    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(cartItem.getQuantity(),cartItem.getUnitPrice(),order, product);
        }).toList();
    }

    private BigDecimal calcTotalPrice(List<OrderItem> orderItemList){
        return orderItemList.stream().map(item ->item.getPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }
}
