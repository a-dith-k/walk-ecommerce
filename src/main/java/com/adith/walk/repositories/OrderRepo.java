package com.adith.walk.repositories;

import com.adith.walk.entities.Customer;
import com.adith.walk.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {


    List<Orders> findCustomerOrdersByCustomer(Customer customer);


}
