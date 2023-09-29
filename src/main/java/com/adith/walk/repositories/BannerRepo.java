package com.adith.walk.repositories;

import com.adith.walk.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepo extends JpaRepository<Banner, Integer> {


    List<Banner> findBannersByBannerPosition(String position);


}
