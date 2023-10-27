package com.liftoff.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "TOKENS")
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "USER_UUID")
    private String userUuid;

    @Column(name = "TOKEN_VALUE")
    private String tokenValue;

    @Column(name = "EXPIRATION_DATE")
    private Instant expirationDate;

    public boolean isValid() {

        return expirationDate != null && Instant.now().isBefore(expirationDate);

    }

}