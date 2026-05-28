package com.example.app.address.model.dto.response;

import lombok.Data;

@Data
public class AddressDetailVO {
    private String id;
    private String userId;
    private String addressType;
    private String recipientName;
    private String recipientPhone;
    private String country;
    private String province;
    private String city;
    private String district;
    private String street;
    private String postalCode;
    private Boolean isDefault;
    private String createdAt;
    private String updatedAt;
}
