package com.adith.walk.repositories;

import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {


    Wishlist findWishlistByCustomer(Customer customer);


}
