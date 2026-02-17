package com.demo.repository;

import java.util.Optional;

/**
 * Repository abstraction for storing and retrieving
 */
public interface TokenRepository {

    void saveMapping(String accountNumber, String token);

    Optional<String> findTokenByAccount(String accountNumber);

    Optional<String> findAccountByToken(String token);

    boolean saveIfAbsent(String accountNumber, String token);

    void saveReverseMapping(String token, String accountNumber);
}