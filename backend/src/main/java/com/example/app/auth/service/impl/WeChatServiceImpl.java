package com.example.app.auth.service.impl;

import com.example.app.auth.config.JwtUtil;
import com.example.app.auth.config.WeChatConfig;
import com.example.app.auth.service.WeChatService;
import com.example.app.common.exception.BusinessException;
import com.example.app.user.mapper.UserMapper;
import com.example.app.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatServiceImpl implements WeChatService {

    private final WeChatConfig weChatConfig;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final ConcurrentHashMap<String, WeChatSession> SCENE_STORE = new ConcurrentHashMap<>();
    private static final long SCENE_TTL_MS = 300_000;

    private record WeChatSession(String scene, long createdAt, String openId, boolean loggedIn) {
        boolean isExpired() { return System.currentTimeMillis() - createdAt > SCENE_TTL_MS; }
    }

    @Override
    public WeChatQrCodeResponse createQrCode(String baseUrl) {
        String scene = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        String qrCodeUrl = generateQrCodeUrl(scene, baseUrl);
        SCENE_STORE.put(scene, new WeChatSession(scene, System.currentTimeMillis(), null, false));
        cleanupExpiredScenes();
        return WeChatQrCodeResponse.pending(scene, qrCodeUrl);
    }

    @Override
    public WeChatLoginResponse handleCallback(String scene, String authCode) {
        WeChatSession session = SCENE_STORE.get(scene);
        if (session == null || session.isExpired()) {
            throw new BusinessException("E9001", "二维码已过期，请重新扫码。");
        }
        if (session.loggedIn()) {
            throw new BusinessException("E9002", "该二维码已被扫描。");
        }

        String openId = exchangeCodeForOpenId(authCode);
        User user = findOrCreateWeChatUser(openId);
        String jwt = jwtUtil.generateToken(user.getId(), user.getDisplayName(), true);
        int expiresIn = (int) jwtUtil.getExpirySeconds(true);

        SCENE_STORE.put(scene, new WeChatSession(scene, session.createdAt(), openId, true));

        return new WeChatLoginResponse(jwt, "Bearer", expiresIn, user.getId(), user.getDisplayName());
    }

    @Override
    @Transactional
    public WeChatQrCodeResponse checkStatus(String scene) {
        WeChatSession session = SCENE_STORE.get(scene);
        if (session == null) {
            throw new BusinessException("E9003", "场景不存在。");
        }
        if (session.isExpired()) {
            SCENE_STORE.remove(scene);
            throw new BusinessException("E9001", "二维码已过期，请重新扫码。");
        }
        if (!session.loggedIn()) {
            return WeChatQrCodeResponse.pending(scene, "");
        }

        User user = userMapper.selectByUsername(session.openId());
        if (user == null) {
            throw new BusinessException("E9004", "用户不存在。");
        }
        String jwt = jwtUtil.generateToken(user.getId(), user.getDisplayName(), false);
        int expiresIn = (int) jwtUtil.getExpirySeconds(false);
        SCENE_STORE.remove(scene);

        return WeChatQrCodeResponse.success(scene, jwt, expiresIn, user.getId(), user.getDisplayName());
    }

    @Override
    @Transactional
    public WeChatLoginResponse mockScan(String scene) {
        WeChatSession session = SCENE_STORE.get(scene);
        if (session == null || session.isExpired()) {
            throw new BusinessException("E9001", "二维码已过期，请重新扫码。");
        }
        if (session.loggedIn()) {
            throw new BusinessException("E9002", "已被扫描。");
        }

        String mockOpenId = "mock_wx_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        User user = findOrCreateWeChatUser(mockOpenId);
        String jwt = jwtUtil.generateToken(user.getId(), user.getDisplayName(), true);
        int expiresIn = (int) jwtUtil.getExpirySeconds(true);

        SCENE_STORE.put(scene, new WeChatSession(scene, session.createdAt(), mockOpenId, true));
        return new WeChatLoginResponse(jwt, "Bearer", expiresIn, user.getId(), user.getDisplayName());
    }

    private String generateQrCodeUrl(String scene, String baseUrl) {
        if (isWeChatConfigured()) {
            try {
                String accessToken = getWeChatAccessToken();
                String json = """
                    {"expire_seconds": 259200, "action_name": "QR_STR_SCENE", "action_info": {"scene": {"scene_str": "%s"}}}
                    """.formatted(scene);
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
                HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String ticket = extractJsonValue(resp.body(), "ticket");
                if (ticket.isBlank()) {
                    throw new BusinessException("E9007", "微信返回空ticket: " + resp.body());
                }
                return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + java.net.URLEncoder.encode(ticket, "UTF-8");
            } catch (Exception e) {
                log.warn("Failed to create WeChat QR code, fallback to mock: {}", e.getMessage());
            }
        }
        String mockUrl = baseUrl + "/api/v1/auth/wechat/mock-scan?scene=" + scene;
        return "/api/v1/auth/wechat/qrcode-image?scene=" + java.net.URLEncoder.encode(mockUrl, java.nio.charset.StandardCharsets.UTF_8);
    }

    private String exchangeCodeForOpenId(String authCode) {
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code"
                .formatted(weChatConfig.getAppId(), weChatConfig.getAppSecret(), authCode);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return extractJsonValue(resp.body(), "openid");
        } catch (Exception e) {
            throw new BusinessException("E9005", "获取微信OpenID失败: " + e.getMessage());
        }
    }

    private String getWeChatAccessToken() {
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s"
                .formatted(weChatConfig.getAppId(), weChatConfig.getAppSecret());
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String token = extractJsonValue(resp.body(), "access_token");
            if (token.isBlank()) {
                throw new BusinessException("E9006", "微信返回空access_token: " + resp.body());
            }
            return token;
        } catch (Exception e) {
            throw new BusinessException("E9006", "获取微信AccessToken失败: " + e.getMessage());
        }
    }

    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start < 0) {
            search = "\"" + key + "\":";
            start = json.indexOf(search);
            if (start < 0) return "";
            start += search.length();
            int end = json.indexOf(",", start);
            if (end < 0) end = json.indexOf("}", start);
            return json.substring(start, end).trim();
        }
        start += search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private boolean isWeChatConfigured() {
        boolean configured = weChatConfig.getAppId() != null && !weChatConfig.getAppId().isBlank()
            && weChatConfig.getAppSecret() != null && !weChatConfig.getAppSecret().isBlank();
        log.debug("WeChat configured: appId='{}', appSecret='{}' → {}", 
            weChatConfig.getAppId(), weChatConfig.getAppSecret() != null ? "***" : null, configured);
        return configured;
    }

    private User findOrCreateWeChatUser(String openId) {
        return Optional.ofNullable(userMapper.selectByUsername(openId)).orElseGet(() -> {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername(openId);
            user.setDisplayName("微信用户_" + openId.substring(openId.length() - 6));
            user.setOpenId(openId);
            user.setEmail("wechat_" + openId.substring(openId.length() - 6) + "@wechat.com");
            user.setPhone("00000000000");
            user.setPasswordHash("wechat");
            user.setMfaEnabled(false);
            user.setMfaSecret("");
            user.setCreatedBy("wechat");
            user.setUpdatedBy("wechat");
            user.setStatus("active");
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setIsDeleted(false);
            userMapper.insert(user);
            return user;
        });
    }

    private void cleanupExpiredScenes() {
        SCENE_STORE.values().removeIf(WeChatSession::isExpired);
    }
}
