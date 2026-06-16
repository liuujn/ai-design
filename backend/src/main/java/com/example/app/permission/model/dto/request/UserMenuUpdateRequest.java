package com.example.app.permission.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class UserMenuUpdateRequest {
    @NotEmpty
    private List<String> menuIds;
}
