package com.example.app.cart.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Cart {
    private String id;
    private String userId;
    private String addressId;
    private String status;
    private java.math.BigDecimal totalAmount;
    private String remark;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isDeleted;
}
