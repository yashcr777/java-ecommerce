package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.CartItemRepository;
import com.yashcode.EcommerceBackend.Repository.CartRepository;
import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.Cart.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
 class CartServiceTests {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotalAmount(BigDecimal.valueOf(200));
    }

    @Test
    void testGetCart_Success() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart retrievedCart = cartService.getCart(1L);

        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(200), cart.getTotalAmount());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testGetCart_NotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.getCart(1L));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testClearCart_Success() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart cart1=new Cart();
        cart1.setId(1L);
        cartService.clearCart(1L);

        assertEquals(BigDecimal.ZERO, cart.getTotalAmount());
        verify(cartItemRepository, times(1)).deleteAllByCartId(1L);
        verify(cartRepository, times(1)).deleteById(1L);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testClearCart_NotFound() {
        when(cartRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Cart not found"));

        assertThrows(ResourceNotFoundException.class, () -> cartService.clearCart(1L));
        verify(cartItemRepository, never()).deleteAllByCartId(1L);
        verify(cartRepository, never()).deleteById(1L);
    }

    @Test
    void testGetTotalPrice_Success() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        BigDecimal totalPrice = cartService.getTotalPrice(1L);

        assertEquals(BigDecimal.valueOf(200), totalPrice);
    }

    @Test
    void testGetTotalPrice_CartNotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.getTotalPrice(1L));
    }

    @Test
    void testInitializeNewCart_ExistingCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        Cart initializedCart = cartService.initializeNewCart(user);

        assertEquals(cart, initializedCart);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testInitializeNewCart_NewCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart newCart = cartService.initializeNewCart(user);

        assertNotNull(newCart);
        assertEquals(user, newCart.getUser());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testGetCartByUserId_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        Cart retrievedCart = cartService.getCartByUserId(1L);

        assertNotNull(retrievedCart);
        assertEquals(1L, retrievedCart.getId());
    }

    @Test
    void testGetCartByUserId_NotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(null);

        Cart retrievedCart = cartService.getCartByUserId(1L);

        assertNull(retrievedCart);
    }
}
