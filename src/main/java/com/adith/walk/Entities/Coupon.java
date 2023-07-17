package com.adith.walk.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Coupon {

    @Id
    private Integer couponId;

}
