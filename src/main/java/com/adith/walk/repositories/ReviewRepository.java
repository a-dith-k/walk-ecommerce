package com.adith.walk.repositories;


import com.adith.walk.entities.Customer;
import com.adith.walk.entities.Product;
import com.adith.walk.entities.ProductReview;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ProductReview, Long> {

    List<ProductReview> getProductReviewsByProductAndIsApprovedTrue(Product product);

    Optional<ProductReview> findProductReviewByProductAndAndCustomer(Product product, Customer customer);

    List<ProductReview> findProductReviewByIsApprovedFalse();

    @Modifying
    @Query("update ProductReview pr set pr.isApproved = true where pr.id = :id")
    void approveReviewById(@Param("id") Long id);

    List<ProductReview> getProductReviewsByProduct(Product productById);

    @Transactional
    @Query("select avg(pr.rating) from ProductReview pr where pr.product=:product and pr.isApproved=true ")
    Optional<Long> findReviewAggregate(@Param("product") Product product);
}
