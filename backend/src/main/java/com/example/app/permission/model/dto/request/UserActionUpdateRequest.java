package com.example.app.permission.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class UserActionUpdateRequest {
    @NotEmpty
    private List<String> actionIds;
}
