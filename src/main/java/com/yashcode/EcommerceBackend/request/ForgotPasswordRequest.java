package com.yashcode.EcommerceBackend.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    private String password;
}
