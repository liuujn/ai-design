package com.example.app.cart.model.dto.response;

import lombok.Data;

@Data
public class CartCheckoutVO {
    private String orderId;
    private String orderNo;
    private String totalAmount;
    private String status;
    private String message;
}
