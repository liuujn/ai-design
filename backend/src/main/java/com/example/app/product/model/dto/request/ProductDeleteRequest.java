package com.example.app.product.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDeleteRequest {
    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
