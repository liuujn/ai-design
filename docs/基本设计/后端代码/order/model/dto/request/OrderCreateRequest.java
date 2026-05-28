package com.example.app.order.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateRequest {

    @NotBlank(message = "客户姓名不能为空")
    @Size(max = 100, message = "客户姓名长度不能超过100个字符")
    private String customerName;

    @Size(max = 500)
    private String remark;

    @NotEmpty(message = "订单明细不能为空")
    @Valid
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        @NotBlank(message = "商品名称不能为空")
        private String itemName;

        private Integer quantity = 1;

        private BigDecimal unitPrice;
    }
}
