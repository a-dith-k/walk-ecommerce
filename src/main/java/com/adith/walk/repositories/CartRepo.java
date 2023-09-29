package com.adith.walk.repositories;

import com.adith.walk.entities.Cart;
import com.adith.walk.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    Cart findCartByCustomer(Customer customer);

}
