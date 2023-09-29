package com.adith.walk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BannerDTO {

    Integer id;


    String name;


    @NotBlank
    String heading;

    @NotBlank
    String description;

    @NotBlank
    String status;


    String bannerPosition;

    String bannerImageSrc;




}
