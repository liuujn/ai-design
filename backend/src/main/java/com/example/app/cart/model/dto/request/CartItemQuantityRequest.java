package com.example.app.cart.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemQuantityRequest {
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;

    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
