package com.chatbot.chatHub.facebook.autoConnect.controller;

import com.chatbot.chatHub.facebook.autoConnect.dto.CreateFbAutoConnectRequest;
import com.chatbot.chatHub.facebook.autoConnect.service.FbAutoConnectService;
import com.chatbot.chatHub.facebook.autoConnect.dto.AutoConnectResponse; // Thêm import mới
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/connection/facebook/auto-connect")
public class FbAutoConnectController {

  private final FbAutoConnectService fbAutoConnectService;

  public FbAutoConnectController(FbAutoConnectService fbAutoConnectService) {
    this.fbAutoConnectService = fbAutoConnectService;
  }

  @PostMapping
  public ResponseEntity<AutoConnectResponse> autoConnect(@Valid @RequestBody CreateFbAutoConnectRequest request,
                          Principal principal) {
    String ownerId = principal.getName();

    System.out.println("📩 Received auto-connect request: " + request);

    // Thay đổi kiểu dữ liệu nhận về
    AutoConnectResponse result = fbAutoConnectService.autoConnect(
        ownerId,
        request.getBotId(),
        request.getUserAccessToken()
    );

    // Trả về đối tượng kết quả chi tiết
    return ResponseEntity.ok(result);
  }
}