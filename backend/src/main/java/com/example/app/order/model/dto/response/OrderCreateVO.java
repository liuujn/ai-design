package com.example.app.order.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderCreateVO {
    private String id;
    private String orderNo;
    private BigDecimal totalAmount;
    private String status;
    private String createdAt;
}
