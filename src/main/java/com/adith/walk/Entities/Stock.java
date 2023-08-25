package com.adith.walk.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Stock {


    public Stock(Integer stock) {

        this.size_12 =stock;
        this.size_11 =stock;
        this.size_10 =stock;
        this.size_9 =stock;
        this.size_8 =stock;
        this.size_7 =stock;
        this.size_6 =stock;
        this.size_5 =stock;
        this.size_4 =stock;
        this.size_3 =stock;
        this.size_2 =stock;
    }

    @SequenceGenerator(initialValue = 5001,
            allocationSize = 1,
            name = "StockIdGenerator",
            sequenceName = "StockIdGenerator")

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "StockIdGenerator")
    private Integer Id;

    @Digits(integer = 5,fraction = 0)
    private Integer size_12;

    @Digits(integer = 5,fraction = 0)
    private Integer size_11;

    @Digits(integer = 5,fraction = 0)
    private Integer size_10;

    @Digits(integer = 5,fraction = 0)
    private Integer size_9;

    @Digits(integer = 5,fraction = 0)
    private Integer size_8;

    @Digits(integer = 5,fraction = 0)
    private Integer size_7;

    @Digits(integer = 5,fraction = 0)
    private Integer size_6;

    @Digits(integer = 5,fraction = 0)
    private Integer size_5;

    @Digits(integer = 5,fraction = 0)
    private Integer size_4;

    @Digits(integer = 5,fraction = 0)
    private Integer size_3;

    @Digits(integer = 5,fraction = 0)
    private Integer size_2;

    @OneToOne(mappedBy = "stock",cascade = CascadeType.ALL)
            @JsonBackReference
    Product product;

}
