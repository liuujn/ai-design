package com.example.user.model.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserListVO {
    private String id;
    private String username;
    private String displayName;
    private String email;
    private String phone;
    private String status;
    private Boolean mfaEnabled;
    private LocalDateTime createdAt;
}
