package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.Cart.ICartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class CartControllerTest {

    @Mock
    private ICartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetCart_Success() {
        // Arrange
        Cart cart = new Cart();
        when(cartService.getCart(anyLong())).thenReturn(cart);

        // Act
        ResponseEntity<ApiResponse> response = cartController.getCart(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetCart_NotFound() {
        // Arrange
        when(cartService.getCart(anyLong())).thenThrow(new ResourceNotFoundException("Cart not found"));

        // Act
        ResponseEntity<ApiResponse> response = cartController.getCart(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testClearCart_Success() {
        // Act
        ResponseEntity<ApiResponse> response = cartController.clearCart(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Clear Cart Success!", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testClearCart_NotFound() {
        // Arrange
        doThrow(new ResourceNotFoundException("Cart not found")).when(cartService).clearCart(anyLong());

        // Act
        ResponseEntity<ApiResponse> response = cartController.clearCart(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetTotalAmount_Success() {
        // Arrange
        BigDecimal totalPrice = new BigDecimal("99.99");
        when(cartService.getTotalPrice(anyLong())).thenReturn(totalPrice);

        // Act
        ResponseEntity<ApiResponse> response = cartController.getTotalAmount(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Total Price", response.getBody().getMessage());
        assertEquals(totalPrice, response.getBody().getData());
    }

    @Test
    void testGetTotalAmount_NotFound() {
        // Arrange
        when(cartService.getTotalPrice(anyLong())).thenThrow(new ResourceNotFoundException("Cart not found"));

        // Act
        ResponseEntity<ApiResponse> response = cartController.getTotalAmount(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
