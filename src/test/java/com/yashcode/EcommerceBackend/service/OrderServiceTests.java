package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.OrderRepository;
import com.yashcode.EcommerceBackend.Repository.ProductRepository;
import com.yashcode.EcommerceBackend.entity.dto.OrderDto;
import com.yashcode.EcommerceBackend.entity.*;
import com.yashcode.EcommerceBackend.entity.enums.OrderStatus;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.Cart.CartService;
import com.yashcode.EcommerceBackend.service.order.OrderService;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IUserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderService orderService;

    private Cart cart;
    private User user;
    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("john");

        Product product = new Product();
        product.setId(1L);
        product.setInventory(10);
        product.setName("Laptop");

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(BigDecimal.valueOf(1000));

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(Set.of(cartItem));

        order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());

        orderDto = new OrderDto();
        orderDto.setId(order.getOrderId());
    }

    @Test
    void testPlaceOrder_Success() {
        when(cartService.getCartByUserId(1L)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.placeOrder(1L);

        assertEquals(order, savedOrder);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService, times(1)).clearCart(cart.getId());
    }

    @Test
    void testPlaceOrder_EmptyCart() {
        cart.setCartItems(Set.of()); // Setting the cart as empty

        when(cartService.getCartByUserId(1L)).thenReturn(cart);

        assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(1L));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetUserOrders_Success() {
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        List<OrderDto> orderDtos = orderService.getUserOrders(1L);

        assertEquals(1, orderDtos.size());
        verify(orderRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetUserOrders_UserMismatch() {
        User anotherUser = new User();
        anotherUser.setId(2L); // Simulating a different user

        when(userService.getAuthenticatedUser()).thenReturn(anotherUser);

        assertThrows(ResourceNotFoundException.class, () -> orderService.getUserOrders(1L));
    }

    @Test
    void testGetUserOrders_EmptyOrders() {
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(orderRepository.findByUserId(1L)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getUserOrders(4L));
    }

    @Test
    void testGetOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        OrderDto foundOrder = orderService.getOrder(1L);

        assertEquals(orderDto, foundOrder);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrder_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrder(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testCalculateTotalAmount() {
        List<OrderItem> orderItems = Arrays.asList(
                new OrderItem(order, new Product(), 2, BigDecimal.valueOf(100)),
                new OrderItem(order, new Product(), 1, BigDecimal.valueOf(200))
        );

        BigDecimal totalAmount = orderService.calculateTotalAmount(orderItems);

        assertEquals(BigDecimal.valueOf(400), totalAmount);
    }

    @Test
    void testConvertToDto() {
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        OrderDto result = orderService.convertToDto(order);

        assertEquals(orderDto, result);
        verify(modelMapper, times(1)).map(order, OrderDto.class);
    }
}
