package com.adith.walk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class OtpLoginRequestDto {
    @NotBlank(message = "Phone Number Should be filled out" )
    @Size(min = 10,max = 10 ,message = "Enter Valid NUmber" )
//    @Pattern(regexp ="/^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$/" )
    private String username;
}
