package com.yashcode.EcommerceBackend.service.Cart;

import com.yashcode.EcommerceBackend.Repository.CartItemRepository;
import com.yashcode.EcommerceBackend.Repository.CartRepository;
import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.CartItem;
import com.yashcode.EcommerceBackend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    public final AtomicLong cartIdGenerator =new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart= cartRepository.findById(id).orElseThrow(()->new RuntimeException("Cart not Found"));
        BigDecimal totalAmount= cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart=getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();
    }
    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable((getCartByUserId(user.getId())))
                .orElseGet(()->{
                    Cart cart=new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }
    @Override
    public Cart getCartByUserId(Long userId)
    {
        return cartRepository.findByUserId(userId);
    }
}