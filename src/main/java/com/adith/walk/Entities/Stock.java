package com.adith.walk.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "product")
public class Stock implements Serializable {


    @SequenceGenerator(initialValue = 5001,
            allocationSize = 1,
            name = "StockIdGenerator",
            sequenceName = "StockIdGenerator")

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "StockIdGenerator")
    long stockId;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "stock")
    List<Size> sizeList =new ArrayList<>();

    @JoinColumn(name = "product_id")
    @OneToOne(cascade = CascadeType.ALL)
    Product product;


    long totalStock;

}
