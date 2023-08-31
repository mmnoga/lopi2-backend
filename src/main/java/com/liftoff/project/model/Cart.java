package com.liftoff.project.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "CARTS")
@Entity
@Data
@RequiredArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "UUID", unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    //@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "CART_ID", referencedColumnName = "ID")
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name = "TOTAL_PRICE")
    private Double totalPrice = 0.0;

    @Column(name = "TOTAL_QUANTITY")
    private Integer totalQuantity = 0;

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "SESSION_ID")
    private Session session;

}

