package com.adith.walk.service;

import com.adith.walk.entities.ConfirmToken;
import com.adith.walk.entities.Customer;
import com.adith.walk.repositories.TokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {

    TokenRepo tokenRepo;


    public void saveConfirmationToken(ConfirmToken token) {
        tokenRepo.save(token);
    }

    public Optional<ConfirmToken> getToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public Optional<ConfirmToken> getTokenByCustomer(Customer customer) {
        return tokenRepo.getConfirmTokenByCustomer(customer);
    }

    public void setConfirmedAt(String token) {
        tokenRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public void deleteAllTokenByCustomer(Customer customer) {

        tokenRepo.deleteConfirmTokenByCustomer(customer);
    }
}
