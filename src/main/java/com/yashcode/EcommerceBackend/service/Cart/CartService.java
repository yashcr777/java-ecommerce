package com.yashcode.EcommerceBackend.service.Cart;

import com.yashcode.EcommerceBackend.Repository.CartItemRepository;
import com.yashcode.EcommerceBackend.Repository.CartRepository;
import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
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
    @Transactional
    @Override
    public void clearCart(Long id) {
        try {
            Cart cart = getCart(id);
            cartItemRepository.deleteAllByCartId(id);
            cart.getCartItems().clear();
            cart.setTotalAmount(BigDecimal.ZERO);
            cartRepository.deleteById(id);
            log.info("Cleared cart Successfully");
        }
        catch (Exception e){
            log.error("Not able to clear cart");
            throw new ResourceNotFoundException(e.getMessage());
        }
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
                    log.info("Cart initialized successfully");
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