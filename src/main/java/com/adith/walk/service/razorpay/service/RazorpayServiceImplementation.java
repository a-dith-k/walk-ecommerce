package com.adith.walk.service.razorpay.service;

import com.adith.walk.entities.RazorPayDetails;
import com.adith.walk.repositories.RazorPayRepository;
import com.adith.walk.service.RazorPayService;
import org.springframework.stereotype.Service;

@Service
public class RazorpayServiceImplementation implements RazorPayService {


    final RazorPayRepository razorPayRepository;

    public RazorpayServiceImplementation(RazorPayRepository razorPayRepository) {
        this.razorPayRepository = razorPayRepository;
    }

    @Override
    public void saveDetails(RazorPayDetails details) {
        razorPayRepository.save(details);
    }
}
