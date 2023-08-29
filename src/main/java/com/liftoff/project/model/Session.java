package com.liftoff.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Table(name = "SESSIONS")
@Entity
@Data
@RequiredArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "UUID", unique = true)
    private UUID uuid;

    @Column(name = "EXPIRATION_TIME")
    private Instant expirationTime;

    @Column(name = "IS_EXPIRED")
    private boolean isExpired;

}
