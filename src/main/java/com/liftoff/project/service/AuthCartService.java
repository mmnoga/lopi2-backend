package com.liftoff.project.service;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.cart.CartNotFoundException;
import com.liftoff.project.exception.product.ProductNotFoundException;

import java.util.UUID;

public interface AuthCartService {

    /**
     * Finds the cart ID associated with the provided username.
     *
     * This method retrieves the cart ID linked to the user with the given username.
     * If a user with the provided username exists and is associated with a cart, the method
     * returns the cart ID. If no such association exists, null is returned.
     *
     * @param username the username for which to find the associated cart ID
     * @return the cart ID associated with the username, or null if not found
     */
    String findCartIdByUsername(String username);

    /**
     * Creates a new cart for the user specified by the username and returns the cart ID associated with the created cart.
     * If a user with the given username exists, a new cart is created and associated with that user. The cart ID of the created cart
     * is returned. If no user with the given username exists, null is returned.
     *
     * @param username the username for which to create a cart
     * @return the cart ID associated with the created cart, or null if the user does not exist
     */
    String createCartForUser(String username);

    /**
     * Processes the addition of a product to the authenticated user's cart.
     *
     * @param username    The username of the authenticated user.
     * @param productUid  The UUID of the product to be added.
     * @param quantity    The quantity of the product to be added.
     * @return A message indicating the success of adding the product to the cart.
     * @throws CartNotFoundException    If the user's cart is not found.
     * @throws ProductNotFoundException If the product with the given UUID is not found.
     */
    String processCartForUser(String username, UUID productUid, int quantity);

    /**
     * Retrieves cart information associated with the user specified by the username.
     * The method retrieves the cart details, including its products, total price, and total quantity,
     * for the user identified by the provided username.
     *
     * @param username the username of the user for whom to retrieve the cart information
     * @return a CartResponseDTO containing cart information, or null if the user's cart is not found
     */
    CartResponseDTO viewCartForUser(String username);

    /**
     * Clears the contents of the cart associated with the user specified by the username.
     * The method locates the user's cart based on the provided username and clears its contents.
     * If the user's cart is not found or is already empty, no action is taken.
     *
     * @param username the username of the user whose cart will be cleared
     */
    void clearCartForUser(String username);

}
