package com.adith.walk.repositories;

import com.adith.walk.Entities.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<Images,String> {





}
