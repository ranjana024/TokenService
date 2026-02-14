package com.test.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response")
public record ErrorResponse(
        @Schema(description = "error code", example = "INVALID INPUT")
        String code,
        @Schema(description = "error message", example = "list is empty")
        String message
) {}

