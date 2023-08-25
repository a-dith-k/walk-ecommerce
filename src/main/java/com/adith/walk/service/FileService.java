package com.adith.walk.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService  {

    String fileUpload(MultipartFile file) throws IOException;



    String uploadBanner(MultipartFile file)throws IOException;
}
