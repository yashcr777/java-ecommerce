package com.yashcode.EcommerceBackend.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private List<ImageDto> images;

    public ProductDto(String name, String brand, BigDecimal price) {
        this.name=name;
        this.brand=brand;
        this.price=price;
    }

    public ProductDto() {

    }
}
