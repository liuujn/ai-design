package com.example.app.user.service;

import com.example.app.user.model.dto.request.*;
import com.example.app.user.model.dto.response.*;

public interface UserService {
    PageResult<UserListVO> list(UserListQuery query);
    UserDetailVO detail(String id);
    UserCreateVO create(UserCreateRequest request, String operatorId);
    UserUpdateVO update(String id, UserUpdateRequest request, String operatorId);
    void delete(String id, UserDeleteRequest request, String operatorId);
    UserStatusVO updateStatus(String id, UserStatusRequest request, String operatorId);
}
