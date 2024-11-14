package com.yashcode.EcommerceBackend.entity.dto;

import com.yashcode.EcommerceBackend.entity.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductDTO {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
