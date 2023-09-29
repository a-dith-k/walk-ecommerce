package com.adith.walk.service;

import com.adith.walk.entities.Orders;
import com.adith.walk.entities.Payment;
import com.adith.walk.enums.PaymentStatus;
import com.adith.walk.repositories.PaymentRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class PaymentServiceImplementation implements PaymentService {

    PaymentRepo paymentRepo;

    @Override
    public Payment doPayment(Long totalAmount, String paymentMethod, PaymentStatus paymentStatus) {

        Payment payment = new Payment();
        payment.setTotalAmount(totalAmount);
        payment.setMethod(paymentMethod);
        payment.setPaymentStatus(paymentStatus);
        payment.setDateTime(LocalDateTime.now());


        return paymentRepo.save(payment);
    }


    @Override
    public void updatePaymentStatus(Orders order, PaymentStatus paymentStatus) {
        Payment paymentByOrders = paymentRepo.findPaymentByOrders(order);

        paymentByOrders.setPaymentStatus(paymentStatus);
        paymentRepo.save(paymentByOrders);
    }
}
