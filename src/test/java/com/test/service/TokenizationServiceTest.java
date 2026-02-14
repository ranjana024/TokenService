package com.test.service;

import com.test.dto.request.DetokenizeRequest;
import com.test.dto.request.TokenizationRequest;
import com.test.exception.InvalidInputException;
import com.test.exception.TokenNotFoundException;
import com.test.mapper.TokenizationMapper;
import com.test.model.request.DetokenizeRequestModel;
import com.test.model.request.TokenizationRequestModel;
import com.test.repository.TokenRepository;
import com.test.util.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TokenizationServiceTest {

    private TokenRepository repository;
    private TokenGenerator tokenGenerator;
    private TokenizationMapper mapper;
    private TokenizationService service;

    @BeforeEach
    void setup() {
        repository = mock(TokenRepository.class);
        tokenGenerator = mock(TokenGenerator.class);
        mapper = mock(TokenizationMapper.class);

        service = new TokenizationService(repository, tokenGenerator, mapper);
    }

    // ---------------------------------------------------------
    // TOKENIZE TESTS
    // ---------------------------------------------------------

    @Test
    void tokenize_shouldReturnTokens() {
        // given
        TokenizationRequest request = new TokenizationRequest(List.of("111"));

//        when(mapper.map(dto)).thenReturn(model);
        when(repository.findTokenByAccount("111")).thenReturn(Optional.empty());
        mockStatic(TokenGenerator.class);
        when(tokenGenerator.generateToken()).thenReturn("token123");
        when(repository.saveIfAbsent("111", "token123")).thenReturn(true);

        // when
        List<String> result = service.tokenize(request);

        // then
        assertThat(result).containsExactly("token123");
        verify(repository).saveReverseMapping("token123", "111");
    }

    @Test
    void tokenize_shouldThrowInvalidInput_whenListIsEmpty() {
        // given
        TokenizationRequest request = new TokenizationRequest(List.of());

        TokenizationRequestModel model = new TokenizationRequestModel();
        model.setAccountNumbers(List.of());

        // when + then
        assertThatThrownBy(() -> service.tokenize(request))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("List is empty");
    }

    @Test
    void tokenize_shouldReturnExistingToken_whenAlreadyExists() {
        // given
        TokenizationRequest request = new TokenizationRequest(List.of("111"));

        TokenizationRequestModel model = new TokenizationRequestModel();
        model.setAccountNumbers(List.of("111"));

        when(repository.findTokenByAccount("111")).thenReturn(Optional.of("existingToken"));

        // when
        List<String> result = service.tokenize(request);

        // then
        assertThat(result).containsExactly("existingToken");
        verify(repository, never()).saveIfAbsent(any(), any());
        verify(repository, never()).saveReverseMapping(any(), any());
    }

    @Test
    void tokenize_shouldHandleRaceCondition_whenSaveIfAbsentReturnsFalse() {
        // given
        TokenizationRequest request = new TokenizationRequest(List.of("111"));

        TokenizationRequestModel model = new TokenizationRequestModel();
        model.setAccountNumbers(List.of("111"));

        when(repository.findTokenByAccount("111")).thenReturn(Optional.empty());
        when(tokenGenerator.generateToken()).thenReturn("token123");
        when(repository.saveIfAbsent("111", "token123")).thenReturn(false);
        when(repository.findTokenByAccount("111")).thenReturn(Optional.of("raceToken"));

        // when
        List<String> result = service.tokenize(request);

        // then
        assertThat(result).containsExactly("raceToken");
    }

    // ---------------------------------------------------------
    // DETOKENIZE TESTS
    // ---------------------------------------------------------

    @Test
    void detokenize_shouldReturnAccountNumbers() {
        // given
        DetokenizeRequest request = new DetokenizeRequest(List.of("token123"));

        DetokenizeRequestModel model = new DetokenizeRequestModel();
        model.setTokens(List.of("token123"));

        when(repository.findAccountByToken("token123")).thenReturn(Optional.of("111"));

        // when
        List<String> result = service.detokenize(request);

        // then
        assertThat(result).containsExactly("111");
    }

    @Test
    void detokenize_shouldThrowTokenNotFound() {
        // given
        DetokenizeRequest request = new DetokenizeRequest(List.of("token123"));

        DetokenizeRequestModel model = new DetokenizeRequestModel();
        model.setTokens(List.of("token123"));

        when(repository.findAccountByToken("token123")).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> service.detokenize(request))
                .isInstanceOf(TokenNotFoundException.class)
                .hasMessageContaining("token123");
    }
}