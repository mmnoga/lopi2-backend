package com.liftoff.project.repository;

import com.liftoff.project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUuid(UUID cartUuid);

}
