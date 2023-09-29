package com.adith.walk.controllers;

import com.adith.walk.entities.Coupon;
import com.adith.walk.exceptions.CouponAlreadyExistsException;
import com.adith.walk.helper.Message;
import com.adith.walk.service.coupon.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/coupons")
public class CouponController {

    final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }


    @GetMapping("")
    public String getAllCoupons(Model model) {

        model.addAttribute("coupons", couponService.getAllCoupons());

        return "/admin/coupon/all";
    }


    @GetMapping("create-new")
    public String createCoupon(@ModelAttribute("coupon") Coupon coupon) {

        return "admin/coupon/add";

    }

    @PostMapping("create-new")
    public String saveCoupon(@ModelAttribute("coupon") Coupon coupon, Model model) {

        try {
            couponService.createCoupon(coupon);

        } catch (CouponAlreadyExistsException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "/admin/coupon/add";
        }

        return "redirect:/admin/coupons";
    }


    @PutMapping("/toggle/{couponId}")
    public String toggleCoupon(@PathVariable Integer couponId) {

        couponService.toggleCoupon(couponId);

        return "redirect:/admin/coupons";
    }

}
