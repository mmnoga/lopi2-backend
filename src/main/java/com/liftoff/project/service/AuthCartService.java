package com.liftoff.project.service;

import com.liftoff.project.controller.cart.response.CartResponseDTO;
import com.liftoff.project.exception.BusinessException;

import java.util.UUID;

public interface AuthCartService {

    /**
     * Finds the cart ID associated with the provided username.
     * <p>
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
     * @param username   The username of the authenticated user.
     * @param productUid The UUID of the product to be added.
     * @param quantity   The quantity of the product to be added.
     * @return A message indicating the success of adding the product to the cart.
     * @throws BusinessException If the user's cart is not found.
     * @throws BusinessException If the product with the given UUID is not found.
     */
    CartResponseDTO processCartForUser(String username, UUID productUid, int quantity);

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

    /**
     * Deletes a product with the specified UUID from the user's shopping cart and the associated database record.
     *
     * @param productUuid The UUID of the product to be removed from the cart.
     * @param username    The username of the user whose cart is being modified.
     * @throws BusinessException If the user's shopping cart is not found.
     * @throws BusinessException If the specified product is not found in the cart.
     */
    void deleteCartProductByUuidForUser(UUID productUuid, String username);

    /**
     * Updates the cart for a user by modifying the quantity of a product in the cart.
     *
     * @param productUuid The UUID of the product to be updated in the cart.
     * @param quantity    The new quantity of the product to be set in the cart. Must be greater than zero.
     * @param username    The username of the user whose cart is to be updated.
     * @return A {@link CartResponseDTO} containing the updated cart information.
     * @throws BusinessException If the specified quantity is less than or equal to zero.
     * @throws BusinessException If the user's cart is not found.
     * @throws BusinessException If the specified product is not found in the cart.
     */
    CartResponseDTO updateCartForUser(UUID productUuid, int quantity, String username);

}