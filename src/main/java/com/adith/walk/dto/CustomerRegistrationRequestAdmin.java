package com.adith.walk.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationRequestAdmin {


    @Size(min = 10, max = 10, message = "enter 10 digits")
    @Digits(integer = 10, fraction = 0, message = "only digits")
    private String mobileNumber;


    private String password;

    @NotBlank
    @Email(message = "enter valid email")
    private String email;

    @NotBlank(message = "required")
    @Size(min = 4, message = "enter at least 4 characters")
    private String firstName;

    @NotBlank
    private String lastName;

}
