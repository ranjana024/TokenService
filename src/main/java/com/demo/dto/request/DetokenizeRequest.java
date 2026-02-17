package com.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record DetokenizeRequest( @NotEmpty
                                 @UniqueElements
                                 @Valid
                                 List<@Pattern(regexp = "^[A-Za-z0-9_-]+$")String> tokens) {}
