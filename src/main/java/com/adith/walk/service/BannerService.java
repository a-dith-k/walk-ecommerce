package com.adith.walk.service;

import com.adith.walk.entities.Banner;
import com.adith.walk.exceptions.BannerNotFoundException;
import com.adith.walk.repositories.BannerRepo;
import com.adith.walk.service.file.service.FileServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class BannerService {


    final BannerRepo bannerRepo;
    final FileServiceImpl fileService;

    public BannerService(BannerRepo bannerRepo, FileServiceImpl fileService) {
        this.bannerRepo = bannerRepo;
        this.fileService = fileService;
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

//        Path path = Path.of("src/main/resources/static/img/banner/" + banner.getImageUrl());
//
//        Files.deleteIfExists(path);

        fileService.deleteImage(banner.getPublicId());

        bannerRepo.delete(banner);


    }

    public List<Banner> getHeaderBanners() {

        return bannerRepo.findBannersByBannerPosition("header");
    }

    @Transactional
    public void createNewBanner(Map<String,String> values, Banner banner) {

        banner.setImageUrl(values.get("secureUrl"));
        banner.setPublicId(values.get("publicId"));

        bannerRepo.save(banner);
    }

    public Banner getRightFooterBanner() {

        return bannerRepo.findBannersByBannerPosition("footer-right").stream().findFirst().orElse(new Banner());
    }

    public List<Banner> getLeftFooterBanners() {

        return bannerRepo.findBannersByBannerPosition("footer-left");
    }
}
