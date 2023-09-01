package com.liftoff.project.service;

import com.liftoff.project.controller.request.CartRequestDTO;
import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.cart.CartNotFoundException;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
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
    Cart processCart(UUID productUuid, int quantity, HttpServletRequest request, HttpServletResponse response);

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

    /**
     * Adds a product to the shopping cart with a specified quantity.
     *
     * @param cart     The shopping cart to which the product should be added.
     * @param product  The product to be added to the shopping cart.
     * @param quantity The quantity of the product to add to the shopping cart.
     */
    Cart addProductToCart(Cart cart, Product product, int quantity);

    /**
     * Checks if the shopping cart contains a sufficient quantity of the given product.
     *
     * @param product  The product to be checked in the shopping cart.
     * @param quantity The required quantity of the product.
     * @param cart     The shopping cart to be checked.
     * @return True if the shopping cart contains at least the specified quantity of the product, otherwise false.
     */
    boolean hasProductEnoughQuantity(Product product, int quantity, Cart cart);

    /**
     * Removes a product from the shopping cart based on its unique identifier (UUID).
     *
     * @param productUuid The unique identifier (UUID) of the product to be removed from the cart.
     * @param request     The HttpServletRequest containing additional information about the request.
     */
    void removeProduct(UUID productUuid, HttpServletRequest request);

    /**
     * Updates the shopping cart with the provided list of cart request DTOs.
     *
     * @param cartRequestDTOList The list of CartRequestDTO objects containing product updates.
     * @param request            The HttpServletRequest containing additional information about the request.
     * @return A CartResponseDTO representing the updated shopping cart after applying the changes.
     */
    CartResponseDTO updateCart(List<CartRequestDTO> cartRequestDTOList, HttpServletRequest request);

    /**
     * Calculates the total price and total quantity of items in the provided shopping cart.
     *
     * @param cart The Cart object for which to calculate the total price and total quantity.
     * @return A Cart object with updated total price and total quantity fields.
     */
    Cart calculateTotalPriceAndTotalQuantity(Cart cart);

}