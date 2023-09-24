package com.adith.walk.service;


import com.adith.walk.Entities.Coupon;
import com.adith.walk.exceptions.CouponAlreadyExistsException;
import com.adith.walk.exceptions.CouponNotFoundException;
import com.adith.walk.exceptions.InvalidCouponException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CouponService {


    void createCoupon(Coupon coupon) throws CouponAlreadyExistsException;

    List<Coupon> getAllCoupons();

    void toggleCoupon(Integer couponId);

    Long applyCoupon(Long amount,Coupon coupon);

    Long removeCoupon(Long discountedAmount,Long originalAmount,Coupon coupon);

    Coupon getCouponById(Integer couponId);

    Coupon getCouponByName(String couponName) throws  InvalidCouponException, CouponNotFoundException;
}
