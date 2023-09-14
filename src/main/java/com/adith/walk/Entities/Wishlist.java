package com.adith.walk.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist implements Serializable {

    @SequenceGenerator(initialValue = 1,
                        allocationSize = 2,
                        sequenceName = "wishlistIdGenerator",
                        name= "wishlistIdGenerator")

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "wishlistIdGenerator")
    private Integer wishlistId;

    @ManyToMany
    private List<Product> products;

    @OneToOne()
    private Customer customer;
}
