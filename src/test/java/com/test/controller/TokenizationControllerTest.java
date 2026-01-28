package com.test.controller;

import com.test.exception.InvalidInputException;
import com.test.exception.TokenNotFoundException;
import com.test.service.TokenizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenizationController.class)
class TokenizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TokenizationService service;

    @Test
    void tokenize_success_whenInputIsValid() throws Exception {
        when(service.tokenize(List.of("account")))
                .thenReturn(List.of("token"));

        mvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"account\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("token"));
    }

    @Test
    void tokenize_fails_whenServiceThrowsInvalidInput() throws Exception {
        when(service.tokenize(List.of()))
                .thenThrow(new InvalidInputException("empty list"));

        mvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tokenize_returns500_whenUnexpectedExceptionOccurs() throws Exception {
        when(service.tokenize(List.of("ACC1")))
                .thenThrow(new RuntimeException("boom"));

        mvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"ACC1\"]"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void detokenize_success_whenTokensAreValid() throws Exception {
        when(service.detokenize(List.of("token")))
                .thenReturn(List.of("account"));

        mvc.perform(post("/api/v1/tokenization/detokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"token\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("account"));
    }

    @Test
    void detokenize_fails_whenTokenNotFound() throws Exception {
        when(service.detokenize(List.of("xyz")))
                .thenThrow(new TokenNotFoundException("No Token"));

        mvc.perform(post("/api/v1/tokenization/detokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"xyz\"]"))
                .andExpect(status().isNotFound());
    }

    @Test
    void detokenize_returns404_whenNoTokenFound() throws Exception {
        when(service.detokenize(List.of("BAD")))
                .thenThrow(new TokenNotFoundException("BAD"));

        mvc.perform(post("/api/v1/tokenization/detokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"BAD\"]"))
                .andExpect(status().isNotFound());
    }
}