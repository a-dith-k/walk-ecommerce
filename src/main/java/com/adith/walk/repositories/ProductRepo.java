package com.adith.walk.repositories;

import com.adith.walk.Entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepo {

    @PersistenceContext
    EntityManager entityManager;

    public List<Product> findMenProducts() {
        return entityManager.createQuery(
                "SELECT p FROM Product p JOIN p.category c WHERE c.men = true",
                Product.class
        ).getResultList();
    }



    public Page<Product> findMenProducts(Pageable pageable) {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p JOIN p.category c WHERE c.men = true",
                Product.class
        );

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());


        return new PageImpl<>(query.getResultList(), pageable, getTotalMenProductsCount());
    }

    private long getTotalMenProductsCount() {
        Query countQuery = entityManager.createQuery(
                "SELECT COUNT(p) FROM Product p JOIN p.category c WHERE c.men = true"
        );
        return (long) countQuery.getSingleResult();
    }




    public Page<Product> findWomenProducts(Pageable pageable) {
        Query query = entityManager.createQuery(
                "SELECT p FROM Product p JOIN p.category c WHERE c.women = true",
                Product.class
        );

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());


        return new PageImpl<>(query.getResultList(), pageable, getTotalMenProductsCount());
    }

    private long getTotalWomenProductsCount() {
        Query countQuery = entityManager.createQuery(
                "SELECT COUNT(p) FROM Product p JOIN p.category c WHERE c.women = true"
        );
        return (long) countQuery.getSingleResult();
    }
}
