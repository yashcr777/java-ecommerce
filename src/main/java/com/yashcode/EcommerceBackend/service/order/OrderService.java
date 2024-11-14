package com.yashcode.EcommerceBackend.service.order;
import com.yashcode.EcommerceBackend.Repository.OrderRepository;
import com.yashcode.EcommerceBackend.Repository.ProductRepository;
import com.yashcode.EcommerceBackend.entity.dto.OrderDto;
import com.yashcode.EcommerceBackend.entity.*;
import com.yashcode.EcommerceBackend.entity.enums.OrderStatus;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.Cart.CartService;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final IUserService userService;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            System.out.println(cart.getTotalAmount());
            System.out.println(cart.getCartItems());
            if(!(cart.getCartItems().isEmpty())) {
                Order order = createOrder(cart);
                List<OrderItem> orderItemList = createOrderItems(order, cart);
                order.setOrderItems(new HashSet<>(orderItemList));
                order.setTotalAmount(calculateTotalAmount(orderItemList));
                Order savedOrder = orderRepository.save(order);
                cartService.clearCart(cart.getId());
                return savedOrder;
            }
            else{
                log.info("Cart is empty!");
                throw new ResourceNotFoundException("Cart is empty");
            }
        }
        catch(ResourceNotFoundException e){
            log.error("User not found or cart is empty with given id so order is not placed!");
            throw new ResourceNotFoundException("User not found or cart is empty");
        }
    }

    private Order createOrder(Cart cart){
        Order order=new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    private List<OrderItem>createOrderItems(Order order,Cart cart){
        return cart.getCartItems().stream().map(cartItem->{
            Product product=cartItem.getProduct();
            product.setInventory(product.getInventory()-cartItem.getQuantity());
            productRepository.save(product);
                return new OrderItem(
                        order,
                        product,
                        cartItem.getQuantity(),
                        cartItem.getUnitPrice());
        }).toList();
    }
    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        try {
            User user=userService.getAuthenticatedUser();
            if(user.getId().equals(userId)) {
                List<Order> orders = orderRepository.findByUserId(userId);
                log.info("Successfully returned list of orders");
                return orders.stream().map(this::convertToDto).toList();
            }
            else{
                log.error("User not found");
                throw new ResourceNotFoundException("User not found");
            }
        }
        catch(Exception e){
            log.info("Order list is empty");
            throw new ResourceNotFoundException("Order with given user id not present");
        }
    }

    @Override
    public OrderDto getOrder(Long orderId)
    {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->{
                    log.info("Order Not found with given orderId");
                    return new ResourceNotFoundException("Order Not Found");
                });
    }

    public BigDecimal calculateTotalAmount(List<OrderItem>orderItemList){
        return orderItemList
                .stream()
                .map(item->item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }
}
