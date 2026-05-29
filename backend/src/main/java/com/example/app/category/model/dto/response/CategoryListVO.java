package com.example.app.category.model.dto.response;

import lombok.Data;

@Data
public class CategoryListVO {
    private String id;
    private String name;
    private String description;
    private Integer sortOrder;
    private String status;
    private String createdAt;
    private String updatedAt;
}
