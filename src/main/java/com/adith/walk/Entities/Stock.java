package com.adith.walk.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {


    @SequenceGenerator(initialValue = 5001,
            allocationSize = 1,
            name = "StockIdGenerator",
            sequenceName = "StockIdGenerator")

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "StockIdGenerator")
    long stockId;

    @OneToMany(cascade = CascadeType.ALL)
    List<Size> sizeList =new ArrayList<>();


    @JsonBackReference
    @OneToOne(mappedBy = "stock")
    Product product;


    long totalStock;

}
