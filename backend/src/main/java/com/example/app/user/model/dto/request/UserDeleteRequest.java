package com.example.app.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDeleteRequest {
    @NotBlank(message = "E018")
    private String updatedAt;
}
