package com.liftoff.project.service;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.ProductNotFoundException;
import com.liftoff.project.exception.ProductOutOfStockException;
import com.liftoff.project.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface CartService {

    /**
     * Creates a new cart and sets a corresponding cookie in the HttpServletResponse.
     *
     * @param response the HttpServletResponse object to which the cookie will be added
     * @return the newly created cart
     */
    Cart createNewCart(HttpServletResponse response);

    /**
     * Retrieves an existing cart associated with the user from the HttpServletRequest,
     * or creates a new cart if none exists, and sets the corresponding cookie in the HttpServletResponse.
     *
     * @param request  the HttpServletRequest object from which to retrieve the existing cart
     * @param response the HttpServletResponse object to which the cookie will be added
     * @return the retrieved or newly created cart
     */
    Cart getOrCreateCart(HttpServletRequest request, HttpServletResponse response);

    /**
     * Retrieves cart information based on the provided cart ID.
     *
     * @param cartId the identifier of the cart for which information is to be retrieved
     * @return a CartResponseDTO containing cart information
     */
    CartResponseDTO getCart(String cartId);

    /**
     * Saves the provided cart to the data store.
     *
     * @param cart the Cart object to be saved
     * @return the saved Cart object
     */
    Cart saveCart(Cart cart);

    /**
     * Clears the contents of the cart associated with the user in the HttpServletRequest.
     *
     * @param request the HttpServletRequest object from which to retrieve the cart to be cleared
     */
    void clearCart(HttpServletRequest request);

    /**
     * Clears the contents of the cart associated with the provided cart ID.
     *
     * @param cartId the identifier of the cart to be cleared
     */
    void clearUserCart(String cartId);

    /**
     * Finds the cart ID associated with the provided username.
     *
     * @param username the username for which to find the associated cart ID
     * @return the cart ID associated with the username, or null if not found
     */
    String findCartIdByUsername(String username);

    /**
     * Creates a cart for the provided username and returns the cart ID.
     *
     * @param username the username for which to create a cart
     * @return the cart ID associated with the created cart
     */
    String createCartForUser(String username);

    /**
     * Adds a product with the provided product UUID to the cart with the given cart ID.
     *
     * @param cartId     the identifier of the cart to which the product will be added
     * @param productUid the UUID of the product to be added
     * @return a message indicating that the product was successfully added to the cart
     * @throws ProductNotFoundException   if the product with the provided UUID is not found
     * @throws ProductOutOfStockException if the product is out of stock and cannot be added
     */
    String addToCart(String cartId, UUID productUid);

    /**
     * Merges the contents of an unauthenticated user's cart with an authenticated user's cart.
     *
     * @param unauthenticatedCartId the cart ID of the unauthenticated user
     * @param authenticatedCartId   the cart ID of the authenticated user
     */
    void mergeCartWithAuthenticatedUser(
            String unauthenticatedCartId, String authenticatedCartId);

}