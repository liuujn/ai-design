package com.example.app.menu.service;

import com.example.app.menu.model.dto.request.MenuCreateRequest;
import com.example.app.menu.model.dto.request.MenuListQuery;
import com.example.app.menu.model.dto.request.MenuUpdateRequest;
import com.example.app.menu.model.dto.response.MenuVO;
import com.example.app.user.model.dto.response.PageResult;
import java.util.List;

public interface MenuService {
    PageResult<MenuVO> list(MenuListQuery query);
    List<MenuVO> sidebar();
    List<MenuVO> sidebar(String userId);
    MenuVO detail(String id);
    MenuVO create(MenuCreateRequest request, String operatorId);
    MenuVO update(String id, MenuUpdateRequest request, String operatorId);
    void delete(String id, String updatedAt, String operatorId);
    MenuVO updateStatus(String id, String status, String updatedAt, String operatorId);
}
