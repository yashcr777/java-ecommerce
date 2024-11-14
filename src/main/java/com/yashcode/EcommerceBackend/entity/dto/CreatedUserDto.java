package com.yashcode.EcommerceBackend.entity.dto;

import lombok.Data;

@Data
public class CreatedUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
