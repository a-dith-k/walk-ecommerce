package com.adith.walk.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    Boolean formal;

    Boolean casual;

    Boolean sports;

    Boolean men;

    Boolean women;

    Boolean kids;

}
