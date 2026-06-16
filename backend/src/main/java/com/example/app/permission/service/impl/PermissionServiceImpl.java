package com.example.app.permission.service.impl;

import com.example.app.permission.mapper.PermissionMapper;
import com.example.app.permission.model.dto.request.UserActionUpdateRequest;
import com.example.app.permission.model.dto.request.UserMenuUpdateRequest;
import com.example.app.permission.model.dto.response.ActionVO;
import com.example.app.permission.model.dto.response.UserActionVO;
import com.example.app.permission.model.dto.response.UserMenuVO;
import com.example.app.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ActionVO> getAllActions() {
        return permissionMapper.findAllActions();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActionVO> getActionsByMenuId(String menuId) {
        return permissionMapper.findActionsByMenuId(menuId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserMenuVO getUserMenus(String userId) {
        UserMenuVO vo = new UserMenuVO();
        vo.setUserId(userId);
        vo.setMenuIds(permissionMapper.findUserMenuIds(userId));
        return vo;
    }

    @Override
    public void updateUserMenus(String userId, UserMenuUpdateRequest request) {
        permissionMapper.deleteAllUserMenus(userId);
        for (String menuId : request.getMenuIds()) {
            permissionMapper.insertUserMenu(userId, menuId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserActionVO getUserActions(String userId) {
        UserActionVO vo = new UserActionVO();
        vo.setUserId(userId);
        vo.setActionIds(permissionMapper.findUserActionIds(userId));
        return vo;
    }

    @Override
    public void updateUserActions(String userId, UserActionUpdateRequest request) {
        permissionMapper.deleteAllUserActions(userId);
        for (String actionId : request.getActionIds()) {
            permissionMapper.insertUserAction(userId, actionId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUserActionCodes(String userId, String menuId) {
        return permissionMapper.findUserActionCodesByMenu(userId, menuId);
    }
}
