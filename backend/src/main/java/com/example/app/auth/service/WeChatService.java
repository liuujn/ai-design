package com.example.app.auth.service;

public interface WeChatService {
    WeChatQrCodeResponse createQrCode(String baseUrl);
    WeChatLoginResponse handleCallback(String scene, String authCode);
    WeChatQrCodeResponse checkStatus(String scene);
    WeChatLoginResponse mockScan(String scene);

    record WeChatQrCodeResponse(String scene, String qrCodeUrl, boolean loggedIn, String accessToken, Integer expiresIn, String userId, String userName) {
        public static WeChatQrCodeResponse pending(String scene, String qrCodeUrl) {
            return new WeChatQrCodeResponse(scene, qrCodeUrl, false, null, null, null, null);
        }
        public static WeChatQrCodeResponse success(String scene, String accessToken, int expiresIn, String userId, String userName) {
            return new WeChatQrCodeResponse(scene, null, true, accessToken, expiresIn, userId, userName);
        }
    }
    record WeChatLoginResponse(String accessToken, String tokenType, int expiresIn, String userId, String userName) {}
}
