package com.adith.walk.service;

import com.adith.walk.entities.Banner;
import com.adith.walk.exceptions.BannerNotFoundException;
import com.adith.walk.repositories.BannerRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class BannerService {


    final BannerRepo bannerRepo;

    public BannerService(BannerRepo bannerRepo) {
        this.bannerRepo = bannerRepo;
    }

    @Transactional
    public void save(Banner banner) {
        bannerRepo.save(banner);
    }

    public List<Banner> getAllBanner() {

        return bannerRepo.findAll();
    }

    public void delete(Integer bannerId) throws IOException, BannerNotFoundException {

        Banner banner = bannerRepo.findById(bannerId).orElseThrow(() -> new BannerNotFoundException("Not found"));

        Path path = Path.of("src/main/resources/static/img/banner/" + banner.getName());

        Files.deleteIfExists(path);


        bannerRepo.delete(banner);


    }

    public List<Banner> getHeaderBanners() {

        return bannerRepo.findBannersByBannerPosition("header");
    }

    @Transactional
    public void createNewBanner(String s, Banner banner) {

        banner.setName(s);

        bannerRepo.save(banner);
    }

    public Banner getRightFooterBanner() {

        return bannerRepo.findBannersByBannerPosition("footer-right").stream().findFirst().orElse(new Banner());
    }

    public List<Banner> getLeftFooterBanners() {

        return bannerRepo.findBannersByBannerPosition("footer-left");
    }
}
