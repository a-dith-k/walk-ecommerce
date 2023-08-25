package com.adith.walk.service;

import com.adith.walk.repositories.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ImageService {

    @Autowired
    ImageRepo imageRepo;



}
