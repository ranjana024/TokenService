package com.demo.controller;

import com.demo.dto.request.DetokenizeRequest;
import com.demo.dto.request.TokenizationRequest;
import com.demo.service.TokenizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenizationController.class)
@AutoConfigureMockMvc(addFilters = false)
class TokenizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenizationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void tokenize_shouldReturnTokens() throws Exception {
        TokenizationRequest request = new TokenizationRequest(List.of("1234567890"));

        Mockito.when(service.tokenize(any())).thenReturn(List.of("token123"));

        mockMvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("token123"));
    }

    @Test
    void detokenize_shouldReturnAccountNumbers() throws Exception {
        DetokenizeRequest request = new DetokenizeRequest(List.of("token123"));

        Mockito.when(service.detokenize(any())).thenReturn(List.of("1234567890"));

        mockMvc.perform(post("/api/v1/tokenization/detokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("1234567890"));
    }

    @Test
    void tokenize_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        TokenizationRequest request = new TokenizationRequest(List.of());

        mockMvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}