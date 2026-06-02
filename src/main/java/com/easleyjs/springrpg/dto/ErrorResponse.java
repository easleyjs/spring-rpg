package com.easleyjs.springrpg.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse (
        String code,
        String message,
        List<String> details,
        String path,
        Instant timestamp
) {
    public static ErrorResponse of(String code, String message, String path) {
        return new ErrorResponse(code, message, List.of(), path, Instant.now());
    }

    public static ErrorResponse of(String code, String message, List<String> details, String path) {
        return new ErrorResponse(code, message, details, path, Instant.now());
    }
}