package com.adith.walk.repositories;

import com.adith.walk.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Optional<Coupon> findCouponByCouponName(String name);

    List<Coupon> findCouponsByIsActiveTrue();


}
