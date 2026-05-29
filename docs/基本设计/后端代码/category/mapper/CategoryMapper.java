package com.example.app.category.mapper;

import com.example.app.category.model.dto.request.CategoryListQuery;
import com.example.app.category.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectPage(CategoryListQuery query);
    Long count(CategoryListQuery query);
    Category selectById(@Param("id") String id);
    int insert(Category category);
    int updateByIdAndUpdatedAt(Category category);
    int logicDeleteByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
    LocalDateTime selectUpdatedAtById(@Param("id") String id);
    List<Category> selectAllActive();
}
