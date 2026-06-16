package com.example.app.permission.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class UserMenuVO {
    private String userId;
    private List<String> menuIds;
}
