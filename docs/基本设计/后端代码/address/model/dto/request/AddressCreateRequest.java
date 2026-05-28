package com.example.app.address.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressCreateRequest {
    @NotBlank(message = "地址类型不能为空")
    private String addressType;

    @NotBlank(message = "收件人姓名不能为空")
    @Size(min = 1, max = 100, message = "收件人姓名长度应为1到100个字符")
    private String recipientName;

    @NotBlank(message = "收件人电话不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "收件人电话格式不正确")
    private String recipientPhone;

    @NotBlank(message = "国家不能为空")
    private String country;

    @NotBlank(message = "省/州不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(min = 1, max = 200, message = "详细地址长度应为1到200个字符")
    private String street;

    @Pattern(regexp = "^\\d{6}$", message = "邮政编码格式不正确")
    private String postalCode;

    private Boolean isDefault;
}
