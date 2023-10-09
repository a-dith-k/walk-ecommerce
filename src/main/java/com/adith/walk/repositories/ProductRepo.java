package com.adith.walk.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepo {

    @PersistenceContext
    EntityManager entityManager;


}
