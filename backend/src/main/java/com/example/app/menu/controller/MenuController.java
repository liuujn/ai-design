package com.example.app.menu.controller;

import com.example.app.menu.model.dto.request.MenuCreateRequest;
import com.example.app.menu.model.dto.request.MenuListQuery;
import com.example.app.menu.model.dto.request.MenuUpdateRequest;
import com.example.app.menu.model.dto.response.MenuVO;
import com.example.app.menu.service.MenuService;
import com.example.app.user.model.dto.response.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/sidebar")
    public ResponseEntity<List<MenuVO>> sidebar() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(menuService.sidebar(userId));
    }

    @GetMapping
    public ResponseEntity<PageResult<MenuVO>> list(@Valid MenuListQuery query) {
        return ResponseEntity.ok(menuService.list(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuVO> detail(@PathVariable String id) {
        return ResponseEntity.ok(menuService.detail(id));
    }

    @PostMapping
    public ResponseEntity<MenuVO> create(@Valid @RequestBody MenuCreateRequest request) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(menuService.create(request, operatorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuVO> update(@PathVariable String id,
                                          @Valid @RequestBody MenuUpdateRequest request) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(menuService.update(id, request, operatorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                        @RequestParam String updatedAt) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        menuService.delete(id, updatedAt, operatorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MenuVO> updateStatus(@PathVariable String id,
                                                 @RequestParam String status,
                                                 @RequestParam String updatedAt) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(menuService.updateStatus(id, status, updatedAt, operatorId));
    }
}
