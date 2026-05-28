package com.example.app.order.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderUpdateRequest {

    @NotBlank(message = "客户姓名不能为空")
    @Size(max = 100)
    private String customerName;

    @Size(max = 500)
    private String remark;

    @Valid
    private List<OrderItemRequest> items;

    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;

    @Data
    public static class OrderItemRequest {
        private String id;
        private String itemName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
