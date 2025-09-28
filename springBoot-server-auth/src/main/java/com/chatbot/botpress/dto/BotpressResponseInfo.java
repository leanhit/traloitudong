package com.chatbot.botpress.dto;

import org.springframework.http.HttpStatusCode;

public class BotpressResponseInfo {
    private HttpStatusCode httpStatus;
    private String responseBody;

    public BotpressResponseInfo(HttpStatusCode httpStatus, String responseBody) {
        this.httpStatus = httpStatus;
        this.responseBody = responseBody;
    }

    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }
}