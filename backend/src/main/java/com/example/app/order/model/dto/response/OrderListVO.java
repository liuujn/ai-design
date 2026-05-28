package com.example.app.order.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderListVO {
    private String id;
    private String orderNo;
    private String customerName;
    private BigDecimal totalAmount;
    private String status;
    private String createdAt;
    private String updatedAt;
}
