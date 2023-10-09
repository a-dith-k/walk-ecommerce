package com.adith.walk.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class OtpLoginRequestDto {
    @NotBlank(message = "should not be blank")
    @Size(min = 10, max = 10, message = "enter 10 digits")
    @Digits(integer = 10, fraction = 0, message = "only numbers allowed")
//    @Pattern(regexp ="/^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$/" )
    private String username;
}
