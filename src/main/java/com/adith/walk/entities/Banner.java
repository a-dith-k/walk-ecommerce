package com.adith.walk.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    @NotBlank
    String imageUrl;

    String publicId;

    @NotBlank
    String heading;

    @NotBlank
    String description;

    @NotBlank
    String status;


    String bannerPosition;


    public Banner() {

    }
}
