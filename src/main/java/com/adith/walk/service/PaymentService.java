package com.adith.walk.service;


import com.adith.walk.entities.Orders;
import com.adith.walk.entities.Payment;
import com.adith.walk.enums.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    Payment doPayment(Long totalAmount, String paymentMethod, PaymentStatus paymentStatus);

    void updatePaymentStatus(Orders order, PaymentStatus paymentStatus);
}
