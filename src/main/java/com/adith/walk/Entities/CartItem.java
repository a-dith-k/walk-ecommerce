package com.adith.walk.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Data
public class CartItem implements Serializable {

    @SequenceGenerator(initialValue =101,
                        allocationSize = 1,
                        sequenceName = "CartItemGenerator",
                        name ="CartItemGenerator" )


    @Id
    @GeneratedValue(generator = "CartItemGenerator",strategy = GenerationType.SEQUENCE)
    Long ItemId;

    @ManyToOne()
    Product product;

    Long quantity;

    Long totalMRP;

    Long totalPrice;

    @ManyToOne()
    @JoinColumn(name = "cart_id")
    Cart cart;


}
