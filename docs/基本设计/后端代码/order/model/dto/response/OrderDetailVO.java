package com.example.app.order.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetailVO {
    private String id;
    private String orderNo;
    private String customerName;
    private BigDecimal totalAmount;
    private String status;
    private String remark;
    private String createdAt;
    private String updatedAt;
    private List<OrderItemVO> items;

    @Data
    public static class OrderItemVO {
        private String id;
        private String itemName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }
}
