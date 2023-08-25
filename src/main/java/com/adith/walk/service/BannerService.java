package com.adith.walk.service;

import com.adith.walk.Entities.Banner;
import com.adith.walk.repositories.BannerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class BannerService {

    @Autowired
    BannerRepo bannerRepo;

    public void save(Banner banner) {
        bannerRepo.save(banner);
    }

    public List<Banner> getAllBanner() {

       return  bannerRepo.findAll();
    }

    public void delete(Integer bannerId) throws IOException {

        Banner banner=bannerRepo.findById(bannerId).get();

        Path path = Path.of("src/main/resources/static/img/banner/"+banner.getName());

        Files.deleteIfExists(path);


        bannerRepo.delete(banner);




    }
}
