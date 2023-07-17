package com.adith.walk.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer productId;

    private String productName;

    private String productMrp;

    private String offerPrice;

    private String brand;

    private String productRating;

    private String productDescription;

    private String productImgId;

    private String productSizeId;

    private String categoryId;
}
