package com.adith.walk.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Data
@Setter
public class OrderItem implements Serializable {


    @SequenceGenerator(
            allocationSize = 1,
            sequenceName = "OrderItemIdGenerator",
            name = "OrderItemIdGenerator")


    @Id
    @GeneratedValue(generator = "OrderItemIdGenerator", strategy = GenerationType.SEQUENCE)
    private Long ItemId;

    @ManyToOne()
    private Product product;

    @ManyToOne
    @JsonBackReference
    @JsonIgnore
    private Size productSize;

    private Long quantity;

    private Long totalMRP;

    private Long totalPrice;

    private Long taxRate;

    private Long tax;
    @JsonBackReference
    @ManyToOne()
    Orders order;
}
