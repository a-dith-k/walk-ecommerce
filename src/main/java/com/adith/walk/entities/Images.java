package com.adith.walk.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Images implements Serializable {

    @SequenceGenerator(initialValue = 1001,
            allocationSize = 1,
            name = "imageIdGenerator",
            sequenceName = "imageIdGenerator")

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imageIdGenerator")
    private Integer imgId;

    String name;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JsonBackReference
    Product product;


}
