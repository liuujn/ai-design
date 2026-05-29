package com.example.app.category.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDeleteRequest {
    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
