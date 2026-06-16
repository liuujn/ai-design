package com.example.app.user.controller;

import com.example.app.user.model.dto.request.*;
import com.example.app.user.model.dto.response.*;
import com.example.app.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResult<UserListVO>> list(@Valid UserListQuery query) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"u001".equals(userId)) {
            query.setId(userId);
        }
        return ResponseEntity.ok(userService.list(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailVO> detail(@PathVariable String id) {
        return ResponseEntity.ok(userService.detail(id));
    }

    @PostMapping
    public ResponseEntity<UserCreateVO> create(@Valid @RequestBody UserCreateRequest request) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.create(request, operatorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateVO> update(@PathVariable String id,
                                                @Valid @RequestBody UserUpdateRequest request) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.update(id, request, operatorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id,
                                        @Valid @RequestBody UserDeleteRequest request) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.delete(id, request, operatorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserStatusVO> updateStatus(@PathVariable String id,
                                                      @Valid @RequestBody UserStatusRequest request) {
        String operatorId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.updateStatus(id, request, operatorId));
    }
}
