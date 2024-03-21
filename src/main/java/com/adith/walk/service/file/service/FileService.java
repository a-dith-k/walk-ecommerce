package com.adith.walk.service.file.service;


import com.adith.walk.exceptions.IncompatibleImageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public interface FileService {

    Map<String,String> fileUpload(MultipartFile file) throws IOException, IncompatibleImageException;

    void deleteImage(String publicId) throws IOException;
    Map<String,String> uploadBanner(MultipartFile file) throws IOException;
}
