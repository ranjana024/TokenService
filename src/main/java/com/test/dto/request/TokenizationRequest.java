package com.test.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;


public record TokenizationRequest( @NotEmpty
                                   @Valid
                                   List<@Pattern(regexp = "\\d{5,10}",
        message = "Account number must be 5–10 digits") String> accountNumbers) {}
