package com.example.app.permission.controller;

import com.example.app.permission.model.dto.request.UserActionUpdateRequest;
import com.example.app.permission.model.dto.request.UserMenuUpdateRequest;
import com.example.app.permission.model.dto.response.ActionVO;
import com.example.app.permission.model.dto.response.UserActionVO;
import com.example.app.permission.model.dto.response.UserMenuVO;
import com.example.app.permission.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/actions")
    public ResponseEntity<List<ActionVO>> getAllActions() {
        return ResponseEntity.ok(permissionService.getAllActions());
    }

    @GetMapping("/actions/by-menu/{menuId}")
    public ResponseEntity<List<ActionVO>> getActionsByMenu(@PathVariable String menuId) {
        return ResponseEntity.ok(permissionService.getActionsByMenuId(menuId));
    }

    @GetMapping("/permissions/menus/{userId}")
    public ResponseEntity<UserMenuVO> getUserMenus(@PathVariable String userId) {
        return ResponseEntity.ok(permissionService.getUserMenus(userId));
    }

    @PutMapping("/permissions/menus/{userId}")
    public ResponseEntity<Void> updateUserMenus(@PathVariable String userId,
                                                  @Valid @RequestBody UserMenuUpdateRequest request) {
        permissionService.updateUserMenus(userId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permissions/actions/{userId}")
    public ResponseEntity<UserActionVO> getUserActions(@PathVariable String userId) {
        return ResponseEntity.ok(permissionService.getUserActions(userId));
    }

    @PutMapping("/permissions/actions/{userId}")
    public ResponseEntity<Void> updateUserActions(@PathVariable String userId,
                                                    @Valid @RequestBody UserActionUpdateRequest request) {
        permissionService.updateUserActions(userId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permissions/my-actions/{menuId}")
    public ResponseEntity<List<String>> getMyActions(@PathVariable String menuId) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(permissionService.getUserActionCodes(userId, menuId));
    }
}
