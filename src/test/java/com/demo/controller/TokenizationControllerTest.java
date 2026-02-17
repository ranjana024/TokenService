package com.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.dto.request.DetokenizeRequest;
import com.demo.dto.request.TokenizationRequest;
import com.demo.service.TokenizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TokenizationControllerTest {

    private MockMvc mockMvc;
    private TokenizationService service;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        service = Mockito.mock(TokenizationService.class);
        TokenizationController controller = new TokenizationController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void tokenize_shouldReturnTokens() throws Exception {
        // given
        TokenizationRequest request = new TokenizationRequest(List.of("1234567890"));

        Mockito.when(service.tokenize(any())).thenReturn(List.of("token123"));

        // when + then
        mockMvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("token123"));
    }

    @Test
    void detokenize_shouldReturnAccountNumbers() throws Exception {
        // given
        DetokenizeRequest request = new DetokenizeRequest(List.of("token123"));

        Mockito.when(service.detokenize(any())).thenReturn(List.of("1234567890"));

        // when + then
        mockMvc.perform(post("/api/v1/tokenization/detokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("1234567890"));
    }

    @Test
    void tokenize_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        // given: invalid request (empty list)
        TokenizationRequest request = new TokenizationRequest(List.of());

        // when + then
        mockMvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}