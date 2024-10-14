package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.dto.OrderDto;
import com.yashcode.EcommerceBackend.entity.Order;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/order")
    public ResponseEntity<ApiResponse>createOrder(@RequestParam Long userId){
        try {
            Order order=orderService.placeOrder(userId);
            OrderDto dto=orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Order placed Successfully",dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse>getOrder(@PathVariable Long orderId){
        try {
            OrderDto dto= orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Success",dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse>getOrderByUserId(@PathVariable Long userId){
        try {
            List<OrderDto>dto=orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Success",dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
