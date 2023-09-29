package com.adith.walk.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Data
public class CartItem implements Serializable {

    @SequenceGenerator(initialValue = 101,
            allocationSize = 1,
            sequenceName = "CartItemGenerator",
            name = "CartItemGenerator")


    @Id
    @GeneratedValue(generator = "CartItemGenerator", strategy = GenerationType.SEQUENCE)
    private Long ItemId;

    @ManyToOne()
    private Product product;

    @ManyToOne
    private Size productSize;

    private Long quantity;

    private Long totalMRP;

    private Long totalPrice;

    @ManyToOne()
    @JoinColumn(name = "cart_id")
    private Cart cart;


}
