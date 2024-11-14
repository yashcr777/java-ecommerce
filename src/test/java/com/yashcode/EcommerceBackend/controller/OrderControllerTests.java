package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.entity.dto.OrderDto;
import com.yashcode.EcommerceBackend.entity.Order;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.order.IOrderService;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderControllerTests {

    @Mock
    private IOrderService orderService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderController=new OrderController(orderService,userService);
    }

    @Test
    void testCreateOrder_Success() {
        // Arrange
        Order order = new Order();
        OrderDto orderDto = new OrderDto();
        when(orderService.placeOrder(anyLong())).thenReturn(order);
        when(orderService.convertToDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<ApiResponse> response = orderController.createOrder(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order placed Successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        verify(orderService, times(1)).placeOrder(anyLong());
        verify(orderService, times(1)).convertToDto(order);
    }

    @Test
    void testCreateOrder_ResourceNotFoundException() {
        // Arrange
        when(orderService.placeOrder(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        // Act
        ResponseEntity<ApiResponse> response = orderController.createOrder(1L);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(orderService, times(1)).placeOrder(anyLong());
    }

    @Test
    void testGetOrder_Success() {
        // Arrange
        OrderDto orderDto = new OrderDto();
        when(orderService.getOrder(anyLong())).thenReturn(orderDto);

        // Act
        ResponseEntity<ApiResponse> response = orderController.getOrder(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        verify(orderService, times(1)).getOrder(anyLong());
    }

    @Test
    void testGetOrder_ResourceNotFoundException() {
        // Arrange
        when(orderService.getOrder(anyLong())).thenThrow(new ResourceNotFoundException("Order not found"));

        // Act
        ResponseEntity<ApiResponse> response = orderController.getOrder(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Order not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(orderService, times(1)).getOrder(anyLong());
    }

    @Test
    void testGetOrderByUserId_Success() {
        // Arrange
        List<OrderDto> orderDtos = new ArrayList<>();
        when(orderService.getUserOrders(anyLong())).thenReturn(orderDtos);

        // Act
        ResponseEntity<ApiResponse> response = orderController.getOrderByUserId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        verify(orderService, times(1)).getUserOrders(anyLong());
    }

    @Test
    void testGetOrderByUserId_ResourceNotFoundException() {
        // Arrange
        when(orderService.getUserOrders(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        // Act
        ResponseEntity<ApiResponse> response = orderController.getOrderByUserId(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(orderService, times(1)).getUserOrders(anyLong());
    }
}
