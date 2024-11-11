package com.yashcode.EcommerceBackend.entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@RequiredArgsConstructor
public class Products {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;


//    private List<Category> category;
//
//    private List<Image> images;
}
