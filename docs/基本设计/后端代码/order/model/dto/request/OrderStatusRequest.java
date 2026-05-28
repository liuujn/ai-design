package com.example.app.order.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusRequest {
    @NotBlank(message = "订单状态不能为空")
    private String status;

    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
