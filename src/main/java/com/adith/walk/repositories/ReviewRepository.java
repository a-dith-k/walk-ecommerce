package com.adith.walk.repositories;


import com.adith.walk.Entities.Product;
import com.adith.walk.Entities.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ProductReview,Long> {

    List<ProductReview>getProductReviewsByProduct(Product product);
}
