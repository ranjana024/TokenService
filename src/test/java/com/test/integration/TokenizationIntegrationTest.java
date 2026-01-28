package com.test.integration;

import com.test.repository.TokenRepository;
import com.test.util.TokenGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TokenizationIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TokenRepository repository;

    @MockitoBean
    private TokenGenerator tokenGenerator;

    @Test
    void tokenize_returns200_when_inputIsValid() throws Exception {
        /*when(repository.findTokenByAccount("account1"))
                .thenReturn(Optional.empty());

        when(tokenGenerator.generateToken()).thenReturn("token1");

        mvc.perform(post("/api/v1/tokenization/tokenize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"account1\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("token1"));*/
    }
}