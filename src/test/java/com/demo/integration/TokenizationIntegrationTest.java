package com.demo.integration;

import com.demo.repository.TokenRepository;
import com.demo.util.TokenGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@WebMvcTest(TokenizationIntegrationTest.class)
class TokenizationIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenRepository repository;

    @MockBean
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