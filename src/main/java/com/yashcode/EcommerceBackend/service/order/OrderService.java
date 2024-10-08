package com.yashcode.EcommerceBackend.service.order;

import com.yashcode.EcommerceBackend.Repository.OrderRepository;
import com.yashcode.EcommerceBackend.Repository.ProductRepository;
import com.yashcode.EcommerceBackend.dto.OrderDto;
import com.yashcode.EcommerceBackend.entity.Cart;
import com.yashcode.EcommerceBackend.entity.Order;
import com.yashcode.EcommerceBackend.entity.OrderItem;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.enums.OrderStatus;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.Cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        Cart cart=cartService.getCartByUserId(userId);
        Order order=createOrder(cart);
        List<OrderItem>orderItemList=createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder=orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order=new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    private List<OrderItem>createOrderItems(Order order,Cart cart){
        return cart.getCartItems().stream().map(cartItem->{
            Product product=cartItem.getProduct();
            product.setInventory(product.getInventory()-cartItem.getQuantity());
            productRepository.save(product);
                return new OrderItem(
                        order,
                        product,
                        cartItem.getQuantity(),
                        cartItem.getUnitPrice());
        }).toList();
    }
    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order>orders=orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto getOrder(Long orderId)
    {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException("Order Not Found"));
    }

    private BigDecimal calculateTotalAmount(List<OrderItem>orderItemList){
        return orderItemList
                .stream()
                .map(item->item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }
}
