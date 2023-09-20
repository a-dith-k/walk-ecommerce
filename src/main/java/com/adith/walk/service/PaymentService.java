package com.adith.walk.service;


import com.adith.walk.Entities.Payment;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    Payment doPayment(Long totalAmount, String paymentMethod, String paymentStatus);
}
