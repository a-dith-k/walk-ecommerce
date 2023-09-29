package com.adith.walk.repositories;

import com.adith.walk.entities.RazorPayDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface RazorPayRepository extends JpaRepository<RazorPayDetails, Long> {


}
