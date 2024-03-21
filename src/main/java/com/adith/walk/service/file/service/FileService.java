package com.adith.walk.service.file.service;


import com.adith.walk.exceptions.IncompatibleImageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {

    String fileUpload(MultipartFile file) throws IOException, IncompatibleImageException;


    String uploadBanner(MultipartFile file) throws IOException;
}
