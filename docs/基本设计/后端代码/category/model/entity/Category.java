package com.example.app.category.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Category {
    private String id;
    private String name;
    private String description;
    private Integer sortOrder;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isDeleted;
}
