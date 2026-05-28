package com.example.user.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank(message = "E005")
    @Size(min = 3, max = 50, message = "E006")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "E007")
    private String username;

    @NotBlank(message = "E008")
    @Size(max = 100, message = "E009")
    private String displayName;

    @NotBlank(message = "E010")
    @Email(message = "E011")
    private String email;

    private String phone;

    @NotBlank(message = "E012")
    @Size(min = 8, max = 128, message = "E013")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "E014")
    private String password;
}
