package com.example.app.product.mapper;

import com.example.app.product.model.dto.request.ProductListQuery;
import com.example.app.product.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> selectPage(ProductListQuery query);
    Long count(ProductListQuery query);
    Product selectById(@Param("id") String id);
    int insert(Product product);
    int updateByIdAndUpdatedAt(Product product);
    int logicDeleteByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
    int updateStatusByIdAndUpdatedAt(Product product);
    LocalDateTime selectUpdatedAtById(@Param("id") String id);
}
