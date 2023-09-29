package com.adith.walk.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class Size implements Serializable {

    @SequenceGenerator(initialValue = 201,
            allocationSize = 1,
            sequenceName = "sizeIdGenerator",
            name = "sizeIdGenerator"
    )

    @Id
    @GeneratedValue(generator = "sizeIdGenerator", strategy = GenerationType.SEQUENCE)
    long sizeId;

    short sizeNumber;

    short sizeLength;

    short sizeWidth;

    long totalStock;

    @JoinColumn(name = "stock_id")
    @ManyToOne(cascade = CascadeType.ALL)
    Stock stock;


}
