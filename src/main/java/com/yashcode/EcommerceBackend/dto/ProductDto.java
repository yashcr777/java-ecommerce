package com.yashcode.EcommerceBackend.dto;

import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.entity.Image;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
//    private List<Category> category;
    private List<ImageDto> images;
}
