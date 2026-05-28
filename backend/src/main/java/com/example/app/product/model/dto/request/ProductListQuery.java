package com.example.app.product.model.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProductListQuery {
    private String keyword;
    private String category;
    private String status;
    @Min(1) private Integer page = 1;
    @Min(1) private Integer size = 20;

    public int getOffset() { return (page - 1) * size; }
}
