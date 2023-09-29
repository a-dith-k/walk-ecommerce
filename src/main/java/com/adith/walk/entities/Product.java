package com.adith.walk.entities;


import com.adith.walk.enums.CustomerCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @NotBlank(message = "required")
    @Column(unique = true)
    private String productName;

    @NotNull(message = "required")
    private Long productMrp;

    @NotNull(message = "required")
    private Long offerPrice;

    @NotBlank(message = "required")
    private String brand;

    @NotNull(message = "required")
    private CustomerCategory customerCategory;


    @NotBlank(message = "required")
    private String productDescription;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Images> list = new ArrayList<>();


    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private ProductCategory productCategory;


    @JsonBackReference
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Stock stock;


}
