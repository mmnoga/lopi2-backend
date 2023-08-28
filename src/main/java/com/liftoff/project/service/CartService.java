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

    /**
     * Process the addition of a product to the cart.
     *
     * Method adds the specified product to the cart associated with the user's session.
     *
     * @param productUuid The UUID of the product to be added to the cart.
     * @param request     The HttpServletRequest representing the current request.
     * @param response    The HttpServletResponse representing the response to the current request.
     */
    void processCart(UUID productUuid, HttpServletRequest request, HttpServletResponse response);

}