package com.adith.walk.service.file.service;

import com.adith.walk.exceptions.IncompatibleImageException;
import com.adith.walk.helper.ImageCropper;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {
    @Value("${project.image}")
    String path;

    @Autowired
    Cloudinary cloudinary;

//    public String randomName() {
//        final String lexicon="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345674890";
//        StringBuilder builder=new StringBuilder();
//        for(int i=0; i<10; i++){
//            builder.append(lexicon.charAt((int)(Math.random()*60+1)));
//        }
//        builder.append(".jpg");
//        return builder.toString();
//    }

    @Override
    public Map<String,String> fileUpload(MultipartFile file) throws IOException, IncompatibleImageException {
        File newFile=null;
        if(file.getOriginalFilename()!=null)
            newFile= new File(file.getOriginalFilename());
        else{
            newFile=new File("temp.tmp");
        }
        try (OutputStream os = new FileOutputStream(newFile)) {
            os.write(file.getBytes());
        }

        //cropping image
        ImageCropper cropper=new ImageCropper();
        byte[] bytes=cropper.cropImage(newFile);
        newFile.delete();

        //cloudinary
        return uploadToCloudinary(bytes);
    }



    public Map<String,String> uploadBanner(MultipartFile file) throws IOException {

        return uploadToCloudinary(file.getBytes());
    }



    private Map<String,String> uploadToCloudinary(byte[] bytes) throws IOException {
        Map data= cloudinary.uploader().upload(bytes,Map.of());
        System.out.println("public id: "+data.get("public_id"));
        return Map.of("secureUrl",(String)data.get("secure_url"),"publicId",(String)data.get("public_id"));
    }

    public void deleteImage(String publicId) throws IOException {
           cloudinary.uploader().destroy(publicId,Map.of());
    }
}
