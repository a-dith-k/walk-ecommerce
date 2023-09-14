package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Size {

    @SequenceGenerator(initialValue = 201,
            allocationSize = 1,
            sequenceName = "sizeIdGenerator",
            name = "sizeIdGenerator"
    )

    @Id
    @GeneratedValue(generator = "sizeIdGenerator",strategy = GenerationType.SEQUENCE)
    long sizeId;

    short sizeNumber;

    short sizeLength;

    short sizeWidth;

    long totalCount;

    @ManyToOne(cascade = CascadeType.ALL)
    Stock stock;


}
