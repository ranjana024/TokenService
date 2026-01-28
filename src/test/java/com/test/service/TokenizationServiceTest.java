package com.test.service;

import com.test.exception.InvalidInputException;
import com.test.exception.TokenNotFoundException;
import com.test.repository.TokenRepository;
import com.test.util.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TokenizationServiceTest {

    private TokenRepository repository;
    private TokenGenerator tokenGenerator;
    private TokenizationService service;

    @BeforeEach
    void setup() {
        repository = mock(TokenRepository.class);
        tokenGenerator = mock(TokenGenerator.class);
        service = new TokenizationService(repository, tokenGenerator);
    }

    @ParameterizedTest
    @CsvSource({
            "account1, token1",
            "account2, token2",
            "account3, token3"
    })
    void tokenize_returnsExistingToken_whenTokenized(String account, String token) {
        //Arrange
        when(repository.findTokenByAccount(account))
                .thenReturn(Optional.of(token));
        //Act
        List<String> result = service.tokenize(List.of(account));
        //Assert
        assertEquals(List.of(token), result);
        verify(repository).findTokenByAccount(account);
        verifyNoInteractions(tokenGenerator);
        verify(repository, never()).saveIfAbsent(any(), any());
    }

    @Test
    void tokenize_generatesAndSavesToken_whenNoExistingToken() {
        //Arrange
        String account = "account1";
        MockedStatic<TokenGenerator> mockedGenerator = mockStatic(TokenGenerator.class);
        when(repository.findTokenByAccount(account))
                .thenReturn(Optional.empty());
        mockedGenerator.when(TokenGenerator::generateToken).thenReturn("NEW_TOKEN");
        when(repository.saveIfAbsent(account, "NEW_TOKEN"))
                .thenReturn(true);
        // Act
        List<String> result = service.tokenize(List.of(account));
        //Assert
        assertEquals(List.of("NEW_TOKEN"), result);
        verify(repository).saveReverseMapping("NEW_TOKEN", account);
    }

    @ParameterizedTest
    @CsvSource({
            "token1, account1",
            "token2, account2",
            "token3, account3"
    })
    void detokenize_returnsAccount_whenTokenExists(String token, String account) {
        //Arrange
        when(repository.findAccountByToken(token))
                .thenReturn(Optional.of(account));
        //Act
        List<String> result = service.detokenize(List.of(token));
        //Asser
        assertEquals(List.of(account), result);
        verify(repository).findAccountByToken(token);
    }

    @ParameterizedTest
    @ValueSource(strings = {"BAD1", "UNKNOWN", "INVALID"})
    void detokenize_throwsException_whenTokenNotFound(String token) {
        //Arrange
        when(repository.findAccountByToken(token))
                .thenReturn(Optional.empty());
        //Act
        TokenNotFoundException ex = assertThrows(
                TokenNotFoundException.class,
                () -> service.detokenize(List.of(token))
        );
        //Assert
        assertEquals("No account number found for token: "+token, ex.getMessage());
        verify(repository).findAccountByToken(token);
    }

    @Test
    void tokenize_throws_InvalidInputException_whenInput_isinvalid(){
       InvalidInputException ex = assertThrows(InvalidInputException.class,
                () -> service.tokenize(List.of()));
        assertEquals("Invalid Input", ex.getMessage());
    }
}