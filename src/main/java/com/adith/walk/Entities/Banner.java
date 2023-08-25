package com.adith.walk.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    @NotBlank
    String name;

    public Banner(String name, String heading, String description, String status) {
        this.name = name;
        this.heading = heading;
        this.description = description;
        this.status = status;
    }

    @NotBlank
    String heading;

    @NotBlank
    String description;

    @NotBlank
    String status;
}
