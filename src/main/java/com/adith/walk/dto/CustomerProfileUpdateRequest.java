package com.adith.walk.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class CustomerProfileUpdateRequest {


    @Size(min = 10, max = 10,message = "enter 10 digits")
    @Digits(integer = 10, fraction = 0,message = "only digits")
    private String mobileNumber;


    @NotBlank
    @Email(message = "enter valid email")
    private String email;

    @NotBlank
    @Size(min =4 ,message = "enter at least 4 characters")
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String gender;

    @Past
    private LocalDate dob;
}
