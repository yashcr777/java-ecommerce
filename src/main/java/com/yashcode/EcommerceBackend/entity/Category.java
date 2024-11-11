package com.yashcode.EcommerceBackend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class Category {

    private Long id;
    private String name;


    private List<Product> products;

    public Category(String name)
    {
        this.name=name;
    }
}
