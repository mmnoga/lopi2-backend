package com.liftoff.project.controller.order.request;

import com.liftoff.project.controller.product.request.ProductRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
