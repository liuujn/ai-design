package com.example.app.product.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateVO {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String categoryId;
    private String status;
    private String createdAt;
}
