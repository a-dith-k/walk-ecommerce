package com.adith.walk.repositories;

import com.adith.walk.Entities.ConfirmToken;
import com.adith.walk.Entities.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<ConfirmToken,Long> {

    Optional<ConfirmToken> findByToken(String token);


    Optional<ConfirmToken> getConfirmTokenByCustomer(Customer customer);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

    @Transactional
    void deleteConfirmTokenByCustomer(Customer customer);

}
