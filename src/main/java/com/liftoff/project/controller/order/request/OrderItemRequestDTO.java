package com.liftoff.project.controller.order.request;

import com.liftoff.project.controller.request.ProductRequestDTO;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.order.CustomerType;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.Salutation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {

        private ProductRequestDTO productRequestDTO;
        private Integer quantity;
        private Double unitPrice;
        private Double subtotal;

}
