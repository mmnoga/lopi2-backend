package com.liftoff.project.service.impl;

import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.Token;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.TokenRepository;
import com.liftoff.project.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final int tokenExpirationMinutes;

    public TokenServiceImpl(
            TokenRepository tokenRepository,
            @Value("${user.register.token.expiration-minutes}") int tokenExpirationMinutes) {
        this.tokenRepository = tokenRepository;
        this.tokenExpirationMinutes = tokenExpirationMinutes;
    }

    @Override
    public Token getTokenByValue(String tokenValue) {

        return tokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() ->
                        new BusinessException("Token with value: " + tokenValue + " not found",
                                HttpStatus.BAD_REQUEST));
    }

    @Override
    public Token generateTokenForUser(User user, int expirationTimeInMinutes) {

        Token newToken = new Token();
        newToken.setUserUuid(user.getUuid().toString());

        String uniqueTokenValue = generateUniqueTokenValue();
        newToken.setTokenValue(uniqueTokenValue);

        Instant expirationDate = Instant.now()
                .plus(expirationTimeInMinutes, ChronoUnit.MINUTES);
        newToken.setExpirationDate(expirationDate);

        return tokenRepository.save(newToken);
    }

    @Override
    public boolean isValid(Token token) {
        return !token
                .getExpirationDate()
                .isBefore(Instant.now());
    }

    @Override
    public void delete(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public void deleteAllExpired() {
        List<Token> expiredTokens =
                tokenRepository.findAll().stream()
                        .filter(userToken -> !isValid(userToken))
                        .toList();

        expiredTokens.forEach(this::delete);
    }

    private String generateUniqueTokenValue() {

        return UUID.randomUUID().toString();
    }

}