package com.example.app.auth.controller;

import com.example.app.auth.service.WeChatService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/wechat")
@RequiredArgsConstructor
public class WeChatController {

    private final WeChatService weChatService;

    @GetMapping("/qrcode")
    public ResponseEntity<WeChatService.WeChatQrCodeResponse> getQrCode(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return ResponseEntity.ok(weChatService.createQrCode(baseUrl));
    }

    @GetMapping("/status")
    public ResponseEntity<WeChatService.WeChatQrCodeResponse> checkStatus(@RequestParam String scene) {
        return ResponseEntity.ok(weChatService.checkStatus(scene));
    }

    @PostMapping("/callback")
    public ResponseEntity<WeChatService.WeChatLoginResponse> callback(
            @RequestParam String scene,
            @RequestParam("auth_code") String authCode) {
        return ResponseEntity.ok(weChatService.handleCallback(scene, authCode));
    }

    @PostMapping("/mock-scan")
    public ResponseEntity<WeChatService.WeChatLoginResponse> mockScan(@RequestParam String scene) {
        return ResponseEntity.ok(weChatService.mockScan(scene));
    }

    @GetMapping(value = "/qrcode-image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getQrCodeImage(@RequestParam String scene,
                                  @RequestParam(defaultValue = "280") int size) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = Map.of(
            EncodeHintType.MARGIN, 1,
            EncodeHintType.CHARACTER_SET, "UTF-8"
        );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(
            writer.encode(scene, BarcodeFormat.QR_CODE, size, size, hints),
            "PNG", out);
        return out.toByteArray();
    }
}
