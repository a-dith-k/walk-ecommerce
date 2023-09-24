package com.adith.walk.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class OrderItem implements Serializable {


    @SequenceGenerator(
            allocationSize = 1,
            sequenceName = "OrderItemIdGenerator",
            name ="OrderItemIdGenerator" )


    @Id
    @GeneratedValue(generator = "OrderItemIdGenerator",strategy = GenerationType.SEQUENCE)
    private Long ItemId;

    @ManyToOne()
    private Product product;

    @ManyToOne
    private Size productSize;

    private Long quantity;

    private Long totalMRP;

    private Long totalPrice;

    @ManyToOne()
    Orders order ;
}
