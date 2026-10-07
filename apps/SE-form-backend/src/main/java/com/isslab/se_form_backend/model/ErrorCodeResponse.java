package com.isslab.se_form_backend.model;


import lombok.Getter;

public class ErrorCodeResponse {
    @Getter
    private final String code;
    @Getter
    private final String message;

    public ErrorCodeResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }


}
