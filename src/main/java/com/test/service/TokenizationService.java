package com.test.service;

import com.test.dto.request.DetokenizeRequest;
import com.test.dto.request.TokenizationRequest;
import com.test.exception.InvalidInputException;
import com.test.exception.TokenNotFoundException;
import com.test.mapper.TokenizationMapper;
import com.test.repository.TokenRepository;
import com.test.util.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TokenizationService {

    private final TokenRepository repository;
    private final TokenGenerator tokenGenerator;
    private final TokenizationMapper mapper;

    public TokenizationService(TokenRepository repository,
                               TokenGenerator tokenGenerator, TokenizationMapper mapper) {
        this.repository = Objects.requireNonNull(repository);
        this.tokenGenerator = Objects.requireNonNull(tokenGenerator);
        this.mapper = mapper;
    }

    /**
     * Tokenizes a list of account numbers.
     */
    public List<String> tokenize(TokenizationRequest tokenizationRequest) {
        //TokenizationRequestModel tokenizationRequestModel = mapper.map(tokenizationRequest);

       var accountNumbers = tokenizationRequest.accountNumbers();

       if(accountNumbers == null || accountNumbers.isEmpty()){
           throw new InvalidInputException("List is empty");
       }


        return accountNumbers.stream()
                .map(this::tokenizeSingle)
                .toList();
    }

    public List<String> detokenize(DetokenizeRequest detokenizeRequest) {
  //       DetokenizeRequestModel detokenizeRequestModelModel = mapper.map(detokenizeRequest);
        var tokens = detokenizeRequest.tokens();
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