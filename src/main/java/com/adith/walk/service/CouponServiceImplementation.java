package com.adith.walk.service;

import com.adith.walk.Entities.Coupon;
import com.adith.walk.enums.DiscountType;
import com.adith.walk.exceptions.CouponAlreadyExistsException;
import com.adith.walk.exceptions.CouponNotFoundException;
import com.adith.walk.exceptions.InvalidCouponException;
import com.adith.walk.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImplementation implements CouponService{

    final CouponRepository couponRepository;

    public CouponServiceImplementation(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public void createCoupon(Coupon coupon) throws CouponAlreadyExistsException {

       if( couponRepository.findCouponByCouponName(coupon.getCouponName()).isPresent()){
           throw new CouponAlreadyExistsException("Coupon already Exists");
       }

        couponRepository.save(coupon);

    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public void toggleCoupon(Integer couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElse(new Coupon());

        coupon.setIsActive(!coupon.getIsActive());
        couponRepository.save(coupon);
    }

    @Override
    public Long applyCoupon(Long amount,Coupon coupon) {

        if(coupon.getDiscountType().equals(DiscountType.PERCENTAGE)){
            return amount-((amount*coupon.getDiscountValue())/100);
        }else if(coupon.getDiscountType().equals(DiscountType.FIXED_AMOUNT)){
            return amount-coupon.getDiscountValue();
        }else{
            return 0L;
        }

    }

    @Override
    public Long removeCoupon(Long discountedAmount,Long originalAmount,Coupon coupon) {
        if(coupon.getDiscountType().equals(DiscountType.PERCENTAGE)){
            return discountedAmount+((originalAmount*coupon.getDiscountValue())/100);
        }else if(coupon.getDiscountType().equals(DiscountType.FIXED_AMOUNT)){
            return discountedAmount+coupon.getDiscountValue();
        }else{
            return 0L;
        }
    }

    @Override
    public Coupon getCouponById(Integer couponId) {
        return couponRepository.findById(couponId).orElse(new Coupon());
    }

    @Override
    public Coupon getCouponByName(String couponName) throws InvalidCouponException, CouponNotFoundException {

    Coupon coupon= couponRepository.findCouponByCouponName(couponName).orElseThrow(()->new CouponNotFoundException("Coupon is Invalid"));

        if(!coupon.getIsActive()){
            throw new InvalidCouponException("Coupon is Expired");
        }


        return coupon;

    }

}
