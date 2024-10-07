package com.yashcode.EcommerceBackend.service.Cart;

import com.yashcode.EcommerceBackend.entity.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId,Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    void updateItemQuantity(Long cartId,Long productId,int quantity);
    CartItem getCartItems(Long cartId, Long productId);
}
