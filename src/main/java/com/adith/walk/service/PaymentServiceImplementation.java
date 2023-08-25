package com.adith.walk.service;

import com.adith.walk.Entities.Payment;
import com.adith.walk.repositories.PaymentRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class PaymentServiceImplementation implements PaymentService{

    PaymentRepo paymentRepo;
    @Override
    public Payment doPayment(Long totalAmount, String paymentMethod,String paymentStatus) {

        Payment payment=new Payment();
        payment.setTotalAmount(totalAmount);
        payment.setMethod(paymentMethod);
        payment.setStatus(paymentStatus);
        payment.setDateTime(LocalDateTime.now());


        return paymentRepo.save(payment);
    }
}
