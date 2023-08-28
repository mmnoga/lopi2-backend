package com.liftoff.project.service;

import com.liftoff.project.controller.response.CartResponseDTO;
import com.liftoff.project.exception.CartNotFoundException;
import com.liftoff.project.exception.ProductNotFoundException;
import com.liftoff.project.exception.ProductOutOfStockException;

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
     * Processes the addition of a product with the specified product UUID to the cart associated with the user specified by the username.
     * The method adds the specified product to the cart associated with the user's session. It calculates the updated total price and total quantity
     * of the cart after adding the product.
     *
     * @param username   the username of the user for whom to process the cart addition
     * @param productUid the UUID of the product to be added to the cart
     * @return a message indicating that the product was successfully added to the user's cart
     * @throws CartNotFoundException       if the user's cart is not found
     * @throws ProductNotFoundException    if the product with the provided UUID is not found
     * @throws ProductOutOfStockException  if the product is out of stock and cannot be added
     */
    String processCartForUser(String username, UUID productUid);

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
