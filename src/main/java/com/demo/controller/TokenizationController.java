package com.demo.controller;

import com.demo.dto.request.DetokenizeRequest;
import com.demo.dto.request.TokenizationRequest;
import com.demo.service.TokenizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tokenization")
@Tag(name = "Tokenization API", description = "Tokenize and detokenize account numbers")
public class TokenizationController {

    private final TokenizationService service;

    public TokenizationController(TokenizationService service) {
        this.service = service;
    }

    @Operation(
            summary = "Tokenize account numbers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens generated",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(ref = "ErrorResponse"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(ref = "ErrorResponse")))
            }
    )
    @PostMapping("/tokenize")
    public ResponseEntity<List<String>> tokenize(@Valid @RequestBody TokenizationRequest  tokenizationRequest) {
        return ResponseEntity.ok(service.tokenize(tokenizationRequest));
    }

    @Operation(
            summary = "Detokenize tokens",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account numbers returned",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(ref = "ErrorResponse"))),
                    @ApiResponse(responseCode = "404", description = "Token not found",
                            content = @Content(schema = @Schema(ref = "ErrorResponse"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(ref = "ErrorResponse")))
            }
    )
    @PostMapping("/detokenize")
    public ResponseEntity<List<String>> detokenize(@Valid @RequestBody DetokenizeRequest detokenizeRequest) {
        return ResponseEntity.ok(service.detokenize(detokenizeRequest));
    }
}
