package com.example.app.permission.service;

import com.example.app.permission.model.dto.request.UserActionUpdateRequest;
import com.example.app.permission.model.dto.request.UserMenuUpdateRequest;
import com.example.app.permission.model.dto.response.ActionVO;
import com.example.app.permission.model.dto.response.UserActionVO;
import com.example.app.permission.model.dto.response.UserMenuVO;

import java.util.List;

public interface PermissionService {
    List<ActionVO> getAllActions();
    List<ActionVO> getActionsByMenuId(String menuId);
    UserMenuVO getUserMenus(String userId);
    void updateUserMenus(String userId, UserMenuUpdateRequest request);
    UserActionVO getUserActions(String userId);
    void updateUserActions(String userId, UserActionUpdateRequest request);
    List<String> getUserActionCodes(String userId, String menuId);
}
