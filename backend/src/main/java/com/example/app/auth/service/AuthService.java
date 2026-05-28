package com.example.app.auth.service;

import com.example.app.auth.model.dto.request.LoginRequest;
import com.example.app.auth.model.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
