package com.yashcode.EcommerceBackend.request;


import lombok.Data;

@Data
public class PaymentInfoRequest {
    private int amount;
    private String currency;
    private String userEmail;
}
