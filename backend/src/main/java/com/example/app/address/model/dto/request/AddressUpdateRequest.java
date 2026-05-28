package com.example.app.address.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressUpdateRequest {
    @NotBlank(message = "地址类型不能为空")
    private String addressType;

    @NotBlank(message = "收件人姓名不能为空")
    @Size(min = 1, max = 100)
    private String recipientName;

    @NotBlank(message = "收件人电话不能为空")
    @Pattern(regexp = "^1\\d{10}$")
    private String recipientPhone;

    @NotBlank(message = "国家不能为空")
    private String country;

    @NotBlank(message = "省/州不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(min = 1, max = 200)
    private String street;

    @Pattern(regexp = "^\\d{6}$")
    private String postalCode;

    private Boolean isDefault;

    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
