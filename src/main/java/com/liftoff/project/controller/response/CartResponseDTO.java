package com.liftoff.project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private List<ProductResponseDTO> products;
    private Double totalPrice;
    private Integer totalQuantity;
    private Instant createdAt;
    private Instant updatedAt;

}
