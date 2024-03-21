package com.adith.walk.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

@Configuration
public class CommonConfiguration {

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public Cloudinary cloudinary(){


        Map<String,String> config=ObjectUtils.asMap(
                "cloud_name", "di93opbeg",
                "api_key", "441972884889748",
                "api_secret", "ub9QAuL2Dw7UWtH0FEa9eHbfo5E");

        return new Cloudinary(config);
    }


}
