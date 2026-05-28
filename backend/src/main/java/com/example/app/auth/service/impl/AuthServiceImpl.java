package com.example.app.auth.service.impl;

import com.example.app.auth.config.JwtUtil;
import com.example.app.auth.model.dto.request.LoginRequest;
import com.example.app.auth.model.dto.response.LoginResponse;
import com.example.app.auth.service.AuthService;
import com.example.app.common.exception.BusinessException;
import com.example.app.user.mapper.UserMapper;
import com.example.app.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("E9108", "用户名或密码不正确。");
        }

        if (!"active".equals(user.getStatus())) {
            throw new BusinessException("E9109", "账户不可用。");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("E9108", "用户名或密码不正确。");
        }

        boolean rememberMe = Boolean.TRUE.equals(request.getRememberMe());
        String token = jwtUtil.generateToken(user.getId(), user.getDisplayName(), rememberMe);
        int expiresIn = (int) jwtUtil.getExpirySeconds(rememberMe);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .userId(user.getId())
                .userName(user.getDisplayName())
                .build();
    }
}
