package com.liftoff.project.model.order;

import com.liftoff.project.model.Cart;
import com.liftoff.project.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(setterPrefix = "with")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "UID")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "ORDER_DATE")
    private Instant orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus status;

    @Column(name = "TOTAL_PRICE")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "DELIVERY_METHOD_ID")
    private DeliveryMethod deliveryMethod;

    @Column(name = "DELIVERY_COST")
    private double deliveryCost;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SHIPPING_ADDRESS_ID")
    private Address shippingAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BILLING_ADDRESS_ID")
    private Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_METHOD_ID")
    private PaymentMethod paymentMethod;

    @Column(name = "TERMS_ACCEPTED")
    private Boolean termsAccepted;

    @Column(name = "CART_UUID")
    private UUID cartUuid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private List<OrderItem> orderItemList;

}
