package com.adith.walk.repositories;

import com.adith.walk.entities.Customer;
import com.adith.walk.entities.Orders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {


    List<Orders> findCustomerOrdersByCustomer(Customer customer);

    @Transactional
    @Query("select sum(order.totalPrice) from Orders order where order.orderDate=:date")
    Long findSumOfOrderPrice(@Param("date") LocalDate date);


    @Transactional
    @Query("select count(order.id) from Orders order where order.orderDate=:date")
    Long findOrderCountOfTheDay(@Param("date") LocalDate now);
}
