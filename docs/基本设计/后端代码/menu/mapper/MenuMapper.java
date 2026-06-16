package com.example.app.menu.mapper;

import com.example.app.menu.model.dto.request.MenuListQuery;
import com.example.app.menu.model.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> selectPage(MenuListQuery query);
    Long count(MenuListQuery query);
    List<Menu> selectAllActiveOrdered();
    Menu selectById(@Param("id") String id);
    int insert(Menu menu);
    int updateByIdAndUpdatedAt(Menu menu);
    int logicDeleteByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
    int updateStatusByIdAndUpdatedAt(Menu menu);
    LocalDateTime selectUpdatedAtById(@Param("id") String id);
}
