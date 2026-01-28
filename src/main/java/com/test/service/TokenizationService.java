package com.test.service;

import com.test.exception.InvalidInputException;
import com.test.exception.TokenNotFoundException;
import com.test.repository.TokenRepository;
import com.test.util.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TokenizationService {

    private final TokenRepository repository;
    private final TokenGenerator tokenGenerator;

    public TokenizationService(TokenRepository repository,
                               TokenGenerator tokenGenerator) {
        this.repository = Objects.requireNonNull(repository);
        this.tokenGenerator = Objects.requireNonNull(tokenGenerator);
    }

    /**
     * Tokenizes a list of account numbers.
     */
    public List<String> tokenize(List<String> accountNumbers) {

        if (accountNumbers == null || accountNumbers.isEmpty()) {
            throw new InvalidInputException("accountNumbers cannot be empty");
        }

        return accountNumbers.stream()
                .map(this::tokenizeSingle)
                .toList();
    }

    public List<String> detokenize(List<String> tokens) {

        return tokens.stream()
                .map(this::detokenizeSingle)
                .toList();
    }

    /**
     * Tokenizes a single account number with idempotency.
     */
    private String tokenizeSingle(String accountNumber) {
        var existing = repository.findTokenByAccount(accountNumber);
       if (existing.isPresent()){
           return existing.get();
       }

       var token = tokenGenerator.generateToken();
       boolean created = repository.saveIfAbsent(accountNumber, token);

       if (!created){
           return repository.findTokenByAccount(accountNumber).get();
       }
       repository.saveReverseMapping(token, accountNumber);
       return token;
    }

    /**
     * Detokenizes a single token.
     */
    private String detokenizeSingle(String token) {

        return repository.findAccountByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));
    }
}