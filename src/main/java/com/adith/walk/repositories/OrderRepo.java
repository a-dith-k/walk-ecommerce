package com.adith.walk.repositories;

import com.adith.walk.Entities.Customer;
import com.adith.walk.Entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepo extends JpaRepository<CustomerOrder,Long> {


    List<CustomerOrder> findCustomerOrdersByCustomer(Customer customer);


}
