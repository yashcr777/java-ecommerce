package com.yashcode.EcommerceBackend.service.Cart;

import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
}