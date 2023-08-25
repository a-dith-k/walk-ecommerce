package com.adith.walk.repositories;

import com.adith.walk.Entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepo extends JpaRepository<Banner,Integer> {


}
