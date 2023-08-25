package com.adith.walk.service;


import com.adith.walk.Entities.Payment;
import com.adith.walk.repositories.PaymentRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface PaymentService {

    Payment doPayment(Long totalAmount, String paymentMethod, String paymentStatus);
}
