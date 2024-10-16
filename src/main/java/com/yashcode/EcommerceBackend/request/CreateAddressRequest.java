package com.yashcode.EcommerceBackend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAddressRequest {
    @NotBlank(message = "Address Name cannot be empty")
    private String name;
    @NotBlank(message = "City cannot be empty")
    private String city;
    @NotBlank(message = "State cannot be empty")
    private String state;
    @NotBlank(message = "Country cannot be empty")
    private String country;
    @NotBlank(message = "PinCode cannot be empty")
    @Size(min = 6, max = 6, message = "{validation.name.size}")
    private Long pinCode;
}
