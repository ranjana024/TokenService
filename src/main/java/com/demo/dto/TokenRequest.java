package com.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank(message = "clientId is required")
        String clientId,

        @NotBlank(message = "clientSecret is required")
        String clientSecret
) {
}
