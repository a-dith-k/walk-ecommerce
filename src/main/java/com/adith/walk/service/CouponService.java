package com.adith.walk.service;


import com.adith.walk.Entities.Coupon;
import com.adith.walk.repositories.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    @Autowired
    CouponRepository couponRepository;

    public List<Coupon>getAllCoupons(){

        return couponRepository.findAll();
    }
}
