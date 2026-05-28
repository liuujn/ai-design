package com.example.user.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(max = 100, message = "E009")
    private String displayName;

    @Email(message = "E011")
    private String email;

    private String phone;

    @Size(min = 8, max = 128, message = "E013")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "E014")
    private String password;

    @NotBlank(message = "E018")
    private String updatedAt;
}
