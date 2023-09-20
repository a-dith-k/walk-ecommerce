package com.adith.walk.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeExclude;

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

    @HashCodeExclude
    @OneToMany(cascade = CascadeType.ALL)
    List<Size> sizeList =new ArrayList<>();

    @HashCodeExclude
    @JsonBackReference
    @OneToOne(mappedBy = "stock")
    Product product;


    long totalStock;

}
