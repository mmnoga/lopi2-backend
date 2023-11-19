package com.liftoff.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "REVIEWS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "UID")
    private UUID uuid;

    @Column(name = "PRODUCT_UID")
    private UUID productUuid;

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "REVIEW_TEXT")
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "IS_VISIBLE")
    private Boolean isVisible;

}