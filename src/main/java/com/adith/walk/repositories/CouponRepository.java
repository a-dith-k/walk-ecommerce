package com.adith.walk.repositories;

import com.adith.walk.Entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    Optional<Coupon> findCouponByCouponName(String name);



}
