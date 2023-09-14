package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConfirmToken implements Serializable {
    @SequenceGenerator(name = "token_id_generator",
                        sequenceName = "token_id_generator",
                        allocationSize = 2)
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
                    generator = "token_id_generator")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;



    @OneToOne(mappedBy = "confirmToken")
    private Customer customer;


    public ConfirmToken(String token,
                        LocalDateTime createdAt,
                        LocalDateTime expiresAt
                       ) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;

    }
}
