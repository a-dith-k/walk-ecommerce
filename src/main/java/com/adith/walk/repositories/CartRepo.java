package com.adith.walk.repositories;

import com.adith.walk.Entities.Cart;
import com.adith.walk.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {

    Cart findCartByCustomer(Customer customer);

}
