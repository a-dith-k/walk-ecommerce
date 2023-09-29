package com.adith.walk.entities;

import com.adith.walk.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class Coupon implements Serializable {


    @SequenceGenerator(name = "couponIdGenerator"
            , sequenceName = "couponIdGenerator",
            allocationSize = 1)

    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "couponIdGenerator")
    private Integer couponId;

    private Boolean isActive;

    private String couponName;

    private DiscountType discountType;

    private Long discountValue;

    private String couponDescription;

    private Long minOrderValue;

    private Long maxOrderValue;

}
