package com.adith.walk.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequest {


    private Long id;

    @NotBlank
    @Size(min =4 ,message = "enter at least 4 characters")
    private String name;

    @NotBlank
    private String building;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    @Size(min = 10, max = 10,message = "enter 10 digits")
    @Digits(integer = 10, fraction = 0,message = "only digits")
    private String mobile;

    private String alternativeMobile;

    @NotNull
    private Integer zipCode;

    @NotBlank
    private String state;

    private String landmark;

    private String latitude;

    private String longitude;
}
