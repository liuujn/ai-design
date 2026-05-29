package com.example.app.cart.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CartVO {
    private String id;
    private String userId;
    private String addressId;
    private String status;
    private String totalAmount;
    private String remark;
    private List<CartItemVO> items;
    private AddressVO address;
    private String createdAt;
    private String updatedAt;

    @Data
    public static class AddressVO {
        private String id;
        private String recipientName;
        private String recipientPhone;
        private String province;
        private String city;
        private String district;
        private String street;
    }

    @Data
    public static class CartItemVO {
        private String id;
        private String productId;
        private String productName;
        private Integer quantity;
        private String unitPrice;
        private String subtotal;
        private String createdAt;
        private String updatedAt;
    }
}
