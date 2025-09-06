package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
public class ImgBBUpload {

    private static final Logger logger = LoggerFactory.getLogger(ImgBBUpload.class);

    @Value("${imgbb.apiKey}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ImgBBUpload(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("❌ File không được rỗng.");
        }

        logger.info("📤 Bắt đầu upload lên ImgBB: {}", file.getOriginalFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        ResponseEntity<Map> response;
        try {
            response = restTemplate.postForEntity(url, requestEntity, Map.class);
        } catch (Exception e) {
            logger.error("❌ Upload thất bại: {}", e.getMessage());
            throw new RuntimeException("Không thể upload ảnh tới ImgBB.");
        }

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            if (data != null && data.get("url") != null) {
                String imageUrl = (String) data.get("url");
                logger.info("✅ Upload thành công! URL: {}", imageUrl);
                return imageUrl;
            }
        }

        logger.error("❌ Upload ảnh thất bại: {}", response.getStatusCode());
        throw new RuntimeException("Upload ảnh thất bại: " + response.getStatusCode());
    }

    public List<String> uploadMultiple(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("❌ Danh sách file không được rỗng.");
        }

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String url = upload(file); // Gọi lại hàm đơn đã có
                urls.add(url);
            } catch (IOException e) {
                logger.error("❌ Lỗi khi upload ảnh: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Không thể upload ảnh: " + file.getOriginalFilename());
            }
        }

        logger.info("✅ Upload {} ảnh thành công.", urls.size());
        return urls;
    }
}