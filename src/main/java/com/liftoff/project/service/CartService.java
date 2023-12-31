package com.liftoff.project.service;

import com.liftoff.project.controller.cart.response.CartResponseDTO;
import com.liftoff.project.exception.BusinessException;
import com.liftoff.project.model.Cart;
import com.liftoff.project.model.Product;
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
     * @throws BusinessException        If no cart with the given ID exists.
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
     * Updates the quantity of a product in the user's shopping cart.
     *
     * @param productUuid The UUID of the product to update.
     * @param quantity    The new quantity for the product.
     * @param request     The HTTP servlet request.
     * @param response    The HTTP servlet response.
     * @return A {@link CartResponseDTO} containing the updated shopping cart details.
     * @throws BusinessException If the quantity is less than or equal to zero,
     *                           or if the product is not found in the cart,
     *                           or if there is insufficient quantity of the product in stock.
     */
    CartResponseDTO updateCart(UUID productUuid, int quantity,
                               HttpServletRequest request, HttpServletResponse response);

    /**
     * Calculates the total price and total quantity of items in the provided shopping cart.
     *
     * @param cart The Cart object for which to calculate the total price and total quantity.
     * @return A Cart object with updated total price and total quantity fields.
     */
    Cart calculateTotalPriceAndTotalQuantity(Cart cart);

    /**
     * Checks if there is enough quantity of the product to edit it to the specified quantity.
     *
     * @param product  The product to check the quantity for.
     * @param quantity The new quantity to edit the product to.
     * @return {@code true} if there is enough quantity to edit the product to the specified quantity, {@code false} otherwise.
     */
    boolean hasProductEnoughQuantityForEdit(Product product, int quantity);

}