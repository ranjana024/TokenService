package com.test.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RedisTokenRepository implements TokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveMapping(String account, String token) {
        redisTemplate.opsForValue().set("account:" + account, token);
        redisTemplate.opsForValue().set("token:" + token, account);
    }

    @Override
    public Optional<String> findTokenByAccount(String account) {
        return Optional.ofNullable(redisTemplate.opsForValue().get("account:" + account));
    }

    @Override
    public Optional<String> findAccountByToken(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get("token:" + token));
    }

    public boolean saveIfAbsent(String account, String token) {
        return redisTemplate.opsForValue()
                .setIfAbsent("account:" + account, token);
    }

    public void saveReverseMapping(String token, String account) {
        redisTemplate.opsForValue().set("token:" + token, account);
    }
}