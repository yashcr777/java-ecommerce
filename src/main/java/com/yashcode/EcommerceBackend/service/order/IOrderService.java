package com.yashcode.EcommerceBackend.service.order;

import com.yashcode.EcommerceBackend.dto.OrderDto;
import com.yashcode.EcommerceBackend.entity.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder (Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto convertToDto(Order order);
}
