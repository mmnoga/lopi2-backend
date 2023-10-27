package com.liftoff.project.repository;

import com.liftoff.project.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByTokenValue(String tokenValue);

}