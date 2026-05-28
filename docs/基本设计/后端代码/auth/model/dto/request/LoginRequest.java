package com.example.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "E9101")
    @Size(min = 1, max = 100, message = "E9102")
    private String username;

    @NotBlank(message = "E9104")
    @Size(min = 8, max = 128, message = "E9105")
    private String password;

    private Boolean rememberMe;

    private String otp;
}
