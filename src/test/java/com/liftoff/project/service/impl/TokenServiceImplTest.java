package com.liftoff.project.service.impl;

import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.Token;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-test.properties")
class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    @Value("${user.register.token.expiration-minutes}")
    private int tokenExpirationMinutes;

    private TokenServiceImpl tokenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenServiceImpl(tokenRepository, tokenExpirationMinutes);
    }

    @Test
    void shouldReturnTokenByItsValue() {
        // given
        String tokenValue = "testTokenValue";
        Token token = new Token();
        token.setTokenValue(tokenValue);


        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Optional.of(token));

        // when
        Token retrievedToken = tokenService.getTokenByValue(tokenValue);

        // then
        assertNotNull(retrievedToken);
        assertEquals(tokenValue, retrievedToken.getTokenValue());
    }

    @Test
    void shouldThrowExceptionWhenTokenNotExist() {
        // given
        String tokenValue = "nonExistentTokenValue";

        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            tokenService.getTokenByValue(tokenValue);
        });

        // then
        assertEquals("Token with value: " + tokenValue + " not found", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void shouldReturnTokenForUser() {
        // given
        User user = new User();
        user.setUuid(UUID.randomUUID());

        when(tokenRepository.save(Mockito.any(Token.class))).thenAnswer(invocation -> {
            Token savedToken = invocation.getArgument(0);
            assertNotNull(savedToken.getUserUuid());
            assertNotNull(savedToken.getTokenValue());
            assertNotNull(savedToken.getExpirationDate());
            return savedToken;
        });

        // when
        Token generatedToken = tokenService.generateTokenForUser(user);

        // then
        assertNotNull(generatedToken);
        assertEquals(user.getUuid().toString(), generatedToken.getUserUuid());
    }

}