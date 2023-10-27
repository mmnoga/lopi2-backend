package com.liftoff.project.service;

import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.Token;
import com.liftoff.project.model.User;

public interface TokenService {

    /**
     * Retrieves a token by its value.
     *
     * @param tokenValue The value of the token to retrieve.
     * @return The token associated with the provided value.
     * @throws BusinessException If the token with the specified value is not found.
     */
    Token getTokenByValue(String tokenValue);

    /**
     * Generates a new authentication token for the specified user and sets its expiration date.
     *
     * @param user              The user for whom the token is generated.
     * @param expirationTimeInMinutes The duration, in minutes, for which the token will be valid.
     * @return A newly generated Token object associated with the user.
     */
    Token generateTokenForUser(User user, int expirationTimeInMinutes);

    /**
     * Deletes the provided token.
     *
     * @param token The token to be deleted.
     */
    void delete(Token token);

    /**
     * Deletes all expired tokens.
     * Tokens are considered expired if they are no longer valid.
     */
    void deleteAllExpired();

    /**
     * Checks if a token is currently valid based on its expiration date.
     *
     * @param token The token to be checked for validity.
     * @return `true` if the token is currently valid (has not expired), `false` otherwise.
     */
    boolean isValid(Token token);

}