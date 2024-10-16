package com.yashcode.EcommerceBackend.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonSerialize
public class ErrorResponse {

    private String message;
    private Timestamp timestamp;

}

