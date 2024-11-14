package com.yashcode.EcommerceBackend.entity.dto;

import lombok.Data;

import java.util.List;
@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
