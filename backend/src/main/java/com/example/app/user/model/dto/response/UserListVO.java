package com.example.app.user.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;
}
