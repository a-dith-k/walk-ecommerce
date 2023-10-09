package com.adith.walk.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordResetRequest {


    String mobile;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "enter valid password")
    String password;
}
