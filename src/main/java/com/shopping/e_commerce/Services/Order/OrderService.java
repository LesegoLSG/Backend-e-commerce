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

/**
 * Service class for managing orders in the e-commerce application.
 */

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

    /**
     * Places a new order for the user.
     *
     * @param userId the ID of the user placing the order
     * @return the saved Order object
     */
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
    /**
     * Creates a new Order object based on the provided Cart.
     *
     * @param cart the Cart from which the order is created
     * @return the newly created Order object
     */
    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    /**
     * Creates a list of OrderItems based on the items in the Cart.
     *
     * @param order the Order to which the OrderItems belong
     * @param cart  the Cart containing the items
     * @return a list of created OrderItem objects
     */
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(cartItem.getQuantity(),cartItem.getUnitPrice(),order, product);
        }).toList();
    }

    /**
     * Calculates the total price of the order based on its OrderItems.
     *
     * @param orderItemList the list of OrderItems
     * @return the total amount as a BigDecimal
     */
    private BigDecimal calcTotalPrice(List<OrderItem> orderItemList){
        return orderItemList.stream().map(item ->item.getPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    /**
     * Retrieves an Order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the corresponding OrderDto
     * @throws ResourceNotFoundException if the order is not found
     */
    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    /**
     * Retrieves all orders associated with a specific user.
     *
     * @param userId the ID of the user whose orders to retrieve
     * @return a list of OrderDto objects for the user
     */
    @Override
    public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }


    /**
     * Converts an Order object to an OrderDto.
     *
     * @param order the Order to convert
     * @return the converted OrderDto
     */
    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }
}
