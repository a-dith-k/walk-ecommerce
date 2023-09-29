package com.adith.walk.service.coupon.service;

import com.adith.walk.entities.Coupon;
import com.adith.walk.entities.Customer;
import com.adith.walk.enums.DiscountType;
import com.adith.walk.exceptions.CouponAlreadyExistsException;
import com.adith.walk.exceptions.CouponNotFoundException;
import com.adith.walk.exceptions.InvalidCouponException;
import com.adith.walk.repositories.CouponRepository;
import com.adith.walk.service.CustomerService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CouponServiceImplementation implements CouponService {

    final CouponRepository couponRepository;

    final CustomerService customerService;

    public CouponServiceImplementation(CouponRepository couponRepository, CustomerService customerService) {
        this.couponRepository = couponRepository;
        this.customerService = customerService;
    }

    @Override
    public void createCoupon(Coupon coupon) throws CouponAlreadyExistsException {

        if (couponRepository.findCouponByCouponName(coupon.getCouponName()).isPresent()) {
            throw new CouponAlreadyExistsException("Coupon already Exists");
        }

        coupon.setIsActive(false);

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
    public Long applyCoupon(Long amount, Coupon coupon) {

        if (coupon.getDiscountType().equals(DiscountType.PERCENTAGE)) {
            return amount - ((amount * coupon.getDiscountValue()) / 100);
        } else if (coupon.getDiscountType().equals(DiscountType.FIXED_AMOUNT)) {
            return amount - coupon.getDiscountValue();
        } else {
            return 0L;
        }

    }

    @Override
    public Long removeCoupon(Long discountedAmount, Long originalAmount, Coupon coupon) {
        if (coupon.getDiscountType().equals(DiscountType.PERCENTAGE)) {
            return discountedAmount + ((originalAmount * coupon.getDiscountValue()) / 100);
        } else if (coupon.getDiscountType().equals(DiscountType.FIXED_AMOUNT)) {
            return discountedAmount + coupon.getDiscountValue();
        } else {
            return 0L;
        }
    }

    @Override
    public Coupon getCouponById(Integer couponId) {
        return couponRepository.findById(couponId).orElse(new Coupon());
    }

    @Override
    public Coupon getCouponByName(String couponName) throws InvalidCouponException, CouponNotFoundException {

        Coupon coupon = couponRepository.findCouponByCouponName(couponName).orElseThrow(() -> new CouponNotFoundException("Coupon is Invalid"));

        if (!coupon.getIsActive()) {
            throw new InvalidCouponException("Coupon is Expired");
        }


        return coupon;

    }

    @Override
    public List<Coupon> getAllActiveCoupons() {
        return couponRepository.findCouponsByIsActiveTrue();
    }

    @Override
    public void provideCoupon(Principal principal, Long totalPrice) {
        couponRepository.findAll().forEach(coupon -> {
            if (totalPrice < coupon.getMaxOrderValue() && totalPrice > coupon.getMinOrderValue()) {
                Customer customer = customerService.getCustomerByMobile(principal.getName());
                customer.setCoupons(List.of(coupon));
                customerService.saveCustomer(customer);
            }

        });
    }

}
