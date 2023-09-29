package com.adith.walk.repositories;


import com.adith.walk.entities.Orders;
import com.adith.walk.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

    Payment findPaymentByOrders(Orders orders);

}
