package com.example.app.permission.model.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class UserActionVO {
    private String userId;
    private List<String> actionIds;
}
