package com.yashcode.EcommerceBackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;


    private Long productId;

    public OrderItem(Order order,Long productId,int quantity,BigDecimal price){
        this.order=order;
        this.productId=productId;
        this.quantity=quantity;
        this.price=price;
    }
}
