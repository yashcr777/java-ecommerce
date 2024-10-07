package com.yashcode.EcommerceBackend.dto;

import lombok.Data;

@Data
public class CreatedUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
