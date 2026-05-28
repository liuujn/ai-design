package com.example.user.controller;

public class SecurityUtils {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    public static void setCurrentUserId(String userId) {
        currentUser.set(userId);
    }

    public static String getCurrentUserId() {
        String userId = currentUser.get();
        return userId != null ? userId : "SYSTEM";
    }

    public static void clear() {
        currentUser.remove();
    }
}
