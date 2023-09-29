package com.adith.walk.service;

import com.adith.walk.entities.RazorPayDetails;
import org.springframework.stereotype.Service;

@Service
public interface RazorPayService {
    void saveDetails(RazorPayDetails details);
}
