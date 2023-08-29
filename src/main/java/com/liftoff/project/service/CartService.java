package com.liftoff.project.service;

import com.liftoff.project.exception.cart.CartNotFoundException;
import com.liftoff.project.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface CartService {

    /**
     * Retrieves an existing cart associated with the user from the HttpServletRequest,
     * or creates a new cart if none exists, and sets the corresponding cookie in the HttpServletResponse.
     *
     * @param request  the HttpServletRequest object from which to retrieve the existing cart
     * @param response the HttpServletResponse object to which the cookie will be added
     * @return the retrieved or newly created cart
     */
    Cart getCartByCookieOrCreateNewCart(HttpServletRequest request, HttpServletResponse response);

    /**
     * Merges the contents of an unauthenticated user's cart with an authenticated user's cart.
     *
     * @param unauthenticatedCartId the cart ID of the unauthenticated user
     * @param authenticatedCartId   the cart ID of the authenticated user
     */
    void mergeCartWithAuthenticatedUser(
            String unauthenticatedCartId, String authenticatedCartId);

    /**
     * Processes the addition of a specified quantity of a product to the cart associated with the given request and response.
     *
     * @param productUuid The UUID of the product to be added.
     * @param quantity    The quantity of the product to be added.
     * @param request     The HttpServletRequest representing the current request.
     * @param response    The HttpServletResponse representing the response to the current request.
     */
    void processCart(UUID productUuid, int quantity, HttpServletRequest request, HttpServletResponse response);

    /**
     * Clears the contents of the cart associated with the user in the HttpServletRequest.
     *
     * @param request the HttpServletRequest object from which to retrieve the cart to be cleared
     */
    void clearCart(HttpServletRequest request);

    /**
     * Retrieves a {@link Cart} entity from the repository based on the provided cart ID.
     *
     * @param cartId The UUID string representing the cart ID to retrieve.
     * @return The retrieved {@link Cart} entity if found.
     * @throws CartNotFoundException If no cart with the given ID exists.
     * @throws IllegalArgumentException If the provided cart ID is not in a valid UUID format.
     */
    Cart getCart(String cartId);

}