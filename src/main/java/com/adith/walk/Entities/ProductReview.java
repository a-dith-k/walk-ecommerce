package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductReview {


    @SequenceGenerator(initialValue = 1,
                        allocationSize = 1,
                        name = "reviewIdGenerator",
                        sequenceName = "reviewIdGenerator")


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "reviewIdGenerator")
    private Long id;

    private String message;

    private Short rating;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Customer customer;

    private Boolean isApproved;



}
