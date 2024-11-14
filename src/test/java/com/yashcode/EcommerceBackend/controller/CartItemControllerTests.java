package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.Cart.ICartItemService;
import com.yashcode.EcommerceBackend.service.Cart.ICartService;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartItemControllerTests {

    @Mock
    private ICartService cartService;

    @Mock
    private ICartItemService cartItemService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private CartItemController cartItemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cartItemController=new CartItemController(cartService,cartItemService,userService);
    }

    @Test
    void testAddItemToCart_Success() {
        // Arrange
        User user = new User();
        Cart cart = new Cart();
        cart.setId(1L);
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(cartService.initializeNewCart(user)).thenReturn(cart);

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.addItemToCart(1L, 2);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Add Item Success", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(cartItemService, times(1)).addItemToCart(1L, 1L, 2);
    }

    @Test
    void testAddItemToCart_ResourceNotFoundException() {
        // Arrange
        when(userService.getAuthenticatedUser()).thenThrow(new ResourceNotFoundException("User not found"));

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.addItemToCart(1L, 2);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Hbqfhvwefu", response.getBody().getMessage()); // Use appropriate message
        assertNull(response.getBody().getData());
    }

    @Test
    void testAddItemToCart_JwtException() {
        // Arrange
        when(userService.getAuthenticatedUser()).thenThrow(new JwtException("JWT token is invalid"));

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.addItemToCart(1L, 2);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("JWT token is invalid", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testRemoveItemFromCart_Success() {
        // Act
        ResponseEntity<ApiResponse> response = cartItemController.removeItemFromCart(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Remove Item Success", response.getBody().getMessage());
        verify(cartItemService, times(1)).removeItemFromCart(1L, 1L);
    }

    @Test
    void testRemoveItemFromCart_ResourceNotFoundException() {
        // Arrange
        doThrow(new ResourceNotFoundException("Cart or Item not found")).when(cartItemService).removeItemFromCart(anyLong(), anyLong());

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.removeItemFromCart(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart or Item not found", response.getBody().getMessage());
        verify(cartItemService, times(1)).removeItemFromCart(1L, 1L);
    }

    @Test
    void testUpdateItemQuantity_Success() {
        // Act
        ResponseEntity<ApiResponse> response = cartItemController.updateItemQuantity(1L, 1L, 5);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Item Success", response.getBody().getMessage());
        verify(cartItemService, times(1)).updateItemQuantity(1L, 1L, 5);
    }

    @Test
    void testUpdateItemQuantity_ResourceNotFoundException() {
        // Arrange
        doThrow(new ResourceNotFoundException("Item not found")).when(cartItemService).updateItemQuantity(anyLong(), anyLong(), anyInt());

        // Act
        ResponseEntity<ApiResponse> response = cartItemController.updateItemQuantity(1L, 1L, 5);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Item not found", response.getBody().getMessage());
        verify(cartItemService, times(1)).updateItemQuantity(1L, 1L, 5);
    }
}
