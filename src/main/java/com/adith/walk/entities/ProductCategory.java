package com.adith.walk.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ProductCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;


    private String categoryName;


    @OneToMany(mappedBy = "productCategory")
    private List<Product> products = new ArrayList<>();

}
