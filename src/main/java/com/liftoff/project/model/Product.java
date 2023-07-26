package com.liftoff.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "PRODUCTS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Integer id;

    @Column(name = "UID")
    private UUID uId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SKU")
    private String sku;

    @Column(name = "REGULAR_PRICE")
    private Double regularPrice;

    @Column(name = "DISCOUNT_PRICE")
    private Double discountPrice;

    @Column(name = "DISCOUNT_PRICE_END_DATE")
    private LocalDateTime discountPriceEndDate;

    @Column(name = "LOWEST_PRICE")
    private Double lowestPrice;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SHORT_DESCRIPTION")
    private String shortDescription;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "PUBLISHED")
    private Boolean published;

    @Column(name = "PRODUCTSCOL")
    private String productscol;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @ManyToMany()
    @JoinTable(
            name = "PRODUCTS_CATEGORIES",
            joinColumns = @JoinColumn(name = "PRODUCTS_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORIES_ID"))
    private Set<Category> categories;
}
