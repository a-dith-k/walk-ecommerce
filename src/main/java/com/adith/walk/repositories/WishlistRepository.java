package com.adith.walk.repositories;

import com.adith.walk.Entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {

    List<Wishlist> findAllByCustomerId(Integer id);
}
