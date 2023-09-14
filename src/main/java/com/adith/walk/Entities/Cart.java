package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Cart implements Serializable {

    @SequenceGenerator(
            initialValue = 101,
            allocationSize = 1,
            name = "cartIdGenerator",
            sequenceName = "cartIdGenerator"
    )

    @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cartIdGenerator")
    Long cartId;


    @OneToMany(mappedBy ="cart",cascade = CascadeType.ALL)
    List<CartItem> items=new ArrayList<>();

    @OneToOne(mappedBy = "cart")
    Customer customer;


    Integer quantity;

    Long totalPrice;

    Long totalMRP;

    Long totalDiscount;

}
