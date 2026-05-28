package com.example.user.service.impl;

import com.example.user.mapper.UserMapper;
import com.example.user.model.dto.request.*;
import com.example.user.model.dto.response.*;
import com.example.user.model.entity.User;
import com.example.user.model.enums.UserStatus;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(readOnly = true)
    public PageResult<UserListVO> list(UserListQuery query) {
        if (query.getPage() < 1) query.setPage(1);
        if (query.getSize() < 1 || query.getSize() > 100) query.setSize(20);

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            if (!UserStatus.isValid(query.getStatus())) {
                throw new BusinessException("E002", "用户状态值无效。");
            }
        }

        long total = userMapper.count(query);
        List<User> users = userMapper.selectPage(query);

        List<UserListVO> content = users.stream().map(u -> {
            UserListVO vo = new UserListVO();
            vo.setId(u.getId());
            vo.setUsername(u.getUsername());
            vo.setDisplayName(u.getDisplayName());
            vo.setEmail(u.getEmail());
            vo.setPhone(u.getPhone());
            vo.setStatus(u.getStatus());
            vo.setMfaEnabled(u.getMfaEnabled());
            vo.setCreatedAt(u.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) total / query.getSize());
        return new PageResult<>(content, query.getPage(), query.getSize(), total, totalPages);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailVO detail(String id) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException("E003", "用户ID不能为空。");
        }

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("E004", "用户不存在。");
        }

        UserDetailVO vo = new UserDetailVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setDisplayName(user.getDisplayName());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setStatus(user.getStatus());
        vo.setMfaEnabled(user.getMfaEnabled());
        vo.setCreatedAt(user.getCreatedAt());
        vo.setUpdatedAt(user.getUpdatedAt());
        return vo;
    }

    @Override
    public UserCreateVO create(UserCreateRequest request, String operatorId) {
        if (userMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException("E015", "该用户账号已存在。");
        }
        if (userMapper.selectByEmail(request.getEmail()) != null) {
            throw new BusinessException("E016", "该邮箱地址已被使用。");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus("active");
        user.setMfaEnabled(false);
        user.setCreatedBy(operatorId);

        userMapper.insert(user);

        UserCreateVO vo = new UserCreateVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setDisplayName(user.getDisplayName());
        return vo;
    }

    @Override
    public UserUpdateVO update(String id, UserUpdateRequest request, String operatorId) {
        User existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E004", "用户不存在。");
        }

        LocalDateTime currentUpdatedAt = userMapper.selectUpdatedAtById(id);
        if (currentUpdatedAt == null) {
            throw new BusinessException("E004", "用户不存在。");
        }

        LocalDateTime requestUpdatedAt = LocalDateTime.parse(request.getUpdatedAt(),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!currentUpdatedAt.equals(requestUpdatedAt)) {
            throw new BusinessException("E017", "数据已被其他用户修改，请刷新后重试。");
        }

        if (request.getEmail() != null && !request.getEmail().equals(existing.getEmail())) {
            if (userMapper.selectByEmailExcludeId(request.getEmail(), id) != null) {
                throw new BusinessException("E016", "该邮箱地址已被使用。");
            }
        }

        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setDisplayName(request.getDisplayName());
        updateUser.setEmail(request.getEmail());
        updateUser.setPhone(request.getPhone());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            updateUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        updateUser.setUpdatedAt(currentUpdatedAt);
        updateUser.setUpdatedBy(operatorId);

        int affected = userMapper.updateByIdAndUpdatedAt(updateUser);
        if (affected == 0) {
            throw new BusinessException("E017", "数据已被其他用户修改，请刷新后重试。");
        }

        User updated = userMapper.selectById(id);
        UserUpdateVO vo = new UserUpdateVO();
        vo.setId(id);
        vo.setUpdatedAt(updated.getUpdatedAt());
        return vo;
    }

    @Override
    public void delete(String id, UserDeleteRequest request, String operatorId) {
        User existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E004", "用户不存在。");
        }

        LocalDateTime currentUpdatedAt = userMapper.selectUpdatedAtById(id);
        LocalDateTime requestUpdatedAt = LocalDateTime.parse(request.getUpdatedAt(),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!currentUpdatedAt.equals(requestUpdatedAt)) {
            throw new BusinessException("E017", "数据已被其他用户修改，请刷新后重试。");
        }

        int affected = userMapper.logicDeleteByIdAndUpdatedAt(id, currentUpdatedAt);
        if (affected == 0) {
            throw new BusinessException("E017", "数据已被其他用户修改，请刷新后重试。");
        }
    }

    @Override
    public UserStatusVO updateStatus(String id, UserStatusRequest request, String operatorId) {
        if (!UserStatus.isValid(request.getStatus())) {
            throw new BusinessException("E002", "用户状态值无效。");
        }

        User existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("E004", "用户不存在。");
        }

        UserStatus.validateTransition(existing.getStatus(), request.getStatus());

        LocalDateTime currentUpdatedAt = userMapper.selectUpdatedAtById(id);
        LocalDateTime requestUpdatedAt = LocalDateTime.parse(request.getUpdatedAt(),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        if (!currentUpdatedAt.equals(requestUpdatedAt)) {
            throw new BusinessException("E017", "数据已被其他用户修改，请刷新后重试。");
        }

        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setStatus(request.getStatus());
        updateUser.setUpdatedAt(currentUpdatedAt);
        updateUser.setUpdatedBy(operatorId);

        int affected = userMapper.updateStatusByIdAndUpdatedAt(updateUser);
        if (affected == 0) {
            throw new BusinessException("E017", "数据已被其他用户修改，请刷新后重试。");
        }

        User updated = userMapper.selectById(id);
        UserStatusVO vo = new UserStatusVO();
        vo.setId(id);
        vo.setStatus(updated.getStatus());
        vo.setUpdatedAt(updated.getUpdatedAt());
        return vo;
    }
}
