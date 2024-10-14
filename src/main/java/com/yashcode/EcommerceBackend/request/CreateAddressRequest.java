package com.yashcode.EcommerceBackend.request;

import lombok.Data;

@Data
public class CreateAddressRequest {
    private String name;
    private String city;
    private String state;
    private String country;
    private Long pinCode;
}
