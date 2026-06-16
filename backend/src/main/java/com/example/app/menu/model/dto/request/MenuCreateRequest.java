package com.example.app.menu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuCreateRequest {
    @Size(max = 36)
    private String parentId;

    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 100, message = "菜单名称长度不能超过100个字符")
    private String label;

    @Size(max = 2000)
    private String icon;

    @Size(max = 100)
    private String pageTitle;

    @Size(max = 500)
    private String pageSrc;

    private Integer sortOrder;

    private Boolean isDivider;

    private String status = "active";
}
