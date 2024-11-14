package com.yashcode.EcommerceBackend.entity.dto;

import com.yashcode.EcommerceBackend.entity.CartItem;

import java.math.BigDecimal;
import java.util.Set;

public class CartDto {
    public Long cartId;
    private Set<CartItem>items;
    private BigDecimal totalAmount;
}
