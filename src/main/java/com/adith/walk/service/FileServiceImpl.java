package com.adith.walk.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService{
    @Value("${project.image}")
    String path;
    @Override
    public String fileUpload(MultipartFile file) throws IOException {
        //filename

        String name=file.getOriginalFilename();


        String filePath=path+ File.separator+name;
        //create path

        File f=new File(path);

        if(!f.exists()){
            f.mkdir();
        }

        //file copy

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return name;
    }
    @Value("${admin.banner}")
    String bannerPath;

    public String uploadBanner(MultipartFile file) throws IOException{

        String originalFilename = file.getOriginalFilename();

        String s = bannerPath + File.separator + originalFilename;

        File f=new File(bannerPath);
        if(!f.exists()){
            f.mkdir();
        }

        Files.copy(file.getInputStream(),Paths.get(s));

        return originalFilename;
    }
}
