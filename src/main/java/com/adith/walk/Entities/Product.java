package com.adith.walk.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
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

    private String productRating;

    @NotBlank(message = "required")
    private String productDescription;

    @OneToMany(mappedBy = "product" ,orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Images>list=new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    private  Stock stock;




}
