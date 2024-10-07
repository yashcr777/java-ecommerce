package com.yashcode.EcommerceBackend.dto;

import java.util.List;

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
