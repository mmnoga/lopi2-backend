package com.liftoff.project.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "ADDRESS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(setterPrefix = "with")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

}
