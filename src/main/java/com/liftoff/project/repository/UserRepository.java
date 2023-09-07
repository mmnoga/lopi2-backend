package com.liftoff.project.repository;

import com.liftoff.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return An {@link Optional} containing the user if found, or an empty {@link Optional} if not found.
     */
    Optional<User> findByUsername(String username);
}
