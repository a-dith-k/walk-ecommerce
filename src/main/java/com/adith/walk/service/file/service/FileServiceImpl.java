package com.adith.walk.service.file.service;

import com.adith.walk.exceptions.IncompatibleImageException;
import com.adith.walk.helper.ImageCropper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    @Value("${project.image}")
    String path;

    public String randomName() {
        final String lexicon="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345674890";
        StringBuilder builder=new StringBuilder();
        for(int i=0; i<10; i++){
            builder.append(lexicon.charAt((int)(Math.random()*60+1)));
        }
        builder.append(".jpg");
        return builder.toString();
    }

    @Override
    public String fileUpload(MultipartFile file) throws IOException, IncompatibleImageException {

        File newFile = new File("src/main/resources/targetFile.tmp");

        try (OutputStream os = new FileOutputStream(newFile)) {
            os.write(file.getBytes());
        }

        //filename
        String name = randomName();
        String filePath = path + File.separator + name;


        //create path
        File f = new File(path);

        if (!f.exists()) {
            f.mkdir();
        }

        File newFileC=new File(filePath);
        //cropping image
        ImageCropper cropper=new ImageCropper();
        cropper.cropImage(newFile,newFileC);

        //file copy
//        Files.copy(file.getInputStream(), Paths.get(filePath));

        return name;
    }


    @Value("${admin.banner}")
    String bannerPath;

    public String uploadBanner(MultipartFile file) throws IOException {

        String originalFilename = getOriginalName(file).concat(".jpg");


        String s = bannerPath + File.separator + originalFilename;

        File f = new File(bannerPath);
        if (!f.exists()) {
            f.mkdir();
        }
        System.out.println("path of :"+Paths.get(s));
        Files.copy(file.getInputStream(), Paths.get(s));

        return originalFilename;
    }

    private String getOriginalName(MultipartFile file) {

        return file.getOriginalFilename();
    }
}
