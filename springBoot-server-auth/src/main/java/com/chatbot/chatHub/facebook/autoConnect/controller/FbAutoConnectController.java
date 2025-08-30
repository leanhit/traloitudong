// src/main/java/com/chatbot/webHub/facebook/autoConnect/controller/FacebookAutoConnectController.java

package com.chatbot.webHub.facebook.autoConnect.controller;

import com.chatbot.webHub.facebook.autoConnect.dto.CreateFbAutoConnectRequest;
import com.chatbot.webHub.facebook.autoConnect.service.FbAutoConnectService; // Sửa tên class Service
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/connection/facebook/auto-connect")
public class FbAutoConnectController {

    private final FbAutoConnectService fbAutoConnectService; // Sửa tên biến

    public FbAutoConnectController(FbAutoConnectService fbAutoConnectService) {
        this.fbAutoConnectService = fbAutoConnectService;
    }

    @PostMapping
    public ResponseEntity<List<String>> createBulkConnections(@Valid @RequestBody CreateFbAutoConnectRequest request, Principal principal) {
        // Dòng code được thêm để in dữ liệu request
        System.out.println("Received request body: " + request);
        
        String ownerId = principal.getName();
        List<String> connectionIds = fbAutoConnectService.createConnections(ownerId, request);
        return ResponseEntity.ok(connectionIds);
    }     
}