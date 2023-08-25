package com.adith.walk.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Images {

    @SequenceGenerator(initialValue = 1001,
                        allocationSize = 1,
    name = "imageIdGenerator",
    sequenceName = "imageIdGenerator")

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "imageIdGenerator")
    private Integer imgId;

    String name;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
            @JsonBackReference
    Product product;


}
