package com.example.app.order.model.dto.response;

import lombok.Data;

@Data
public class OrderStatusVO {
    private String id;
    private String status;
    private String updatedAt;
}
