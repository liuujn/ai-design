package com.example.app.permission.mapper;

import com.example.app.permission.model.dto.response.ActionVO;
import com.example.app.permission.model.dto.response.UserActionVO;
import com.example.app.permission.model.dto.response.UserMenuVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Select("SELECT id, menu_id AS menuId, action_code AS actionCode, action_name AS actionName FROM sys_action ORDER BY menu_id, action_code")
    List<ActionVO> findAllActions();

    @Select("SELECT id, menu_id AS menuId, action_code AS actionCode, action_name AS actionName FROM sys_action WHERE menu_id = #{menuId} ORDER BY action_code")
    List<ActionVO> findActionsByMenuId(String menuId);

    @Select("SELECT menu_id FROM sys_user_menu WHERE user_id = #{userId}")
    List<String> findUserMenuIds(String userId);

    @Insert("INSERT INTO sys_user_menu (user_id, menu_id) VALUES (#{userId}, #{menuId})")
    void insertUserMenu(@Param("userId") String userId, @Param("menuId") String menuId);

    @Delete("DELETE FROM sys_user_menu WHERE user_id = #{userId}")
    void deleteAllUserMenus(String userId);

    @Select("SELECT action_id FROM sys_user_action WHERE user_id = #{userId}")
    List<String> findUserActionIds(String userId);

    @Insert("INSERT INTO sys_user_action (user_id, action_id) VALUES (#{userId}, #{actionId})")
    void insertUserAction(@Param("userId") String userId, @Param("actionId") String actionId);

    @Delete("DELETE FROM sys_user_action WHERE user_id = #{userId}")
    void deleteAllUserActions(String userId);

    @Select("SELECT a.action_code FROM sys_user_action ua JOIN sys_action a ON a.id = ua.action_id WHERE ua.user_id = #{userId} AND a.menu_id = #{menuId}")
    List<String> findUserActionCodesByMenu(@Param("userId") String userId, @Param("menuId") String menuId);

    @Insert("INSERT INTO sys_action (id, menu_id, action_code, action_name) VALUES (#{id}, #{menuId}, #{actionCode}, #{actionName})")
    void insertAction(@Param("id") String id, @Param("menuId") String menuId, @Param("actionCode") String actionCode, @Param("actionName") String actionName);
}
