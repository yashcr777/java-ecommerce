package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.CartItemRepository;
import com.yashcode.EcommerceBackend.Repository.CartRepository;
import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.CartItem;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.Cart.CartItemService;
import com.yashcode.EcommerceBackend.service.Cart.CartService;
import com.yashcode.EcommerceBackend.service.product.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
 class CartItemServiceTests {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private IProductService productService;

    @Mock
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartItemService cartItemService;

    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(100));

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(5);
        cartItem.setUnitPrice(BigDecimal.valueOf(100));
        cartItem.setTotalPrice(BigDecimal.valueOf(200));

        cart = new Cart();
        cart.setId(1L);
        cart.setCartItems(new HashSet<>(Set.of(cartItem)));
        cart.setTotalAmount(BigDecimal.valueOf(200));
    }

    @Test
    void testAddItemToCart_NewItem() {
        when(cartService.getCart(1L)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(product);

        cart.getCartItems().clear(); // Simulate empty cart for a new item

        cartItemService.addItemToCart(1L, 1L, 2);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(BigDecimal.valueOf(200), cart.getTotalAmount());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testAddItemToCart_ExistingItem() {
        when(cartService.getCart(1L)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(product);

        cartItemService.addItemToCart(1L, 1L, 5); // Adding more quantity to existing item

        assertEquals(2, cart.getCartItems().size());
        assertEquals(5, cartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(cartItem);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveItemFromCart_Success() {
        when(cartService.getCart(1L)).thenReturn(cart);
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

        cartItemService.removeItemFromCart(1L, 1L);

        assertEquals(0, cart.getCartItems().size());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveItemFromCart_ItemNotFound() {
        when(cartService.getCart(1L)).thenReturn(cart);
        cart.getCartItems().clear(); // Simulate cart without the item

        assertThrows(ResourceNotFoundException.class, () -> cartItemService.removeItemFromCart(1L, 1L));
        verify(cartRepository, never()).save(cart);
    }

    @Test
    void testUpdateItemQuantity_Success() {
        when(cartService.getCart(1L)).thenReturn(cart);

        cartItemService.updateItemQuantity(1L, 1L, 5);

        assertEquals(5, cartItem.getQuantity());
        assertEquals(BigDecimal.valueOf(500), cart.getTotalAmount()); // Updating the total price
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateItemQuantity_ItemNotFound() {
        when(cartService.getCart(1L)).thenReturn(cart);
        assertThrows(ResourceNotFoundException.class, () -> cartItemService.updateItemQuantity(2L, 2L, 5));
        verify(cartRepository, never()).save(cart);
    }

    @Test
    void testGetCartItems_Success() {
        when(cartService.getCart(1L)).thenReturn(cart);

        CartItem foundItem = cartItemService.getCartItems(1L, 1L);

        assertEquals(cartItem, foundItem);
    }

    @Test
    void testGetCartItems_ItemNotFound() {
        when(cartService.getCart(1L)).thenReturn(cart);

        assertThrows(ResourceNotFoundException.class, () -> cartItemService.getCartItems(1L, 2L));
    }
}
