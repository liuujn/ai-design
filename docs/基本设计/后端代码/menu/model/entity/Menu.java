package com.example.app.menu.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Menu {
    private String id;
    private String parentId;
    private String label;
    private String icon;
    private String pageTitle;
    private String pageSrc;
    private Integer sortOrder;
    private Boolean isDivider;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isDeleted;
}
