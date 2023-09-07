package com.liftoff.project.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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

    @Column(name = "UID", unique = true)
    private UUID uId;

    @Column(name = "EXPIRATION_TIME")
    private Instant expirationTime;

    @Column(name = "IS_EXPIRED")
    private boolean isExpired;

    @OneToOne(mappedBy = "session", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Cart cart;

}
