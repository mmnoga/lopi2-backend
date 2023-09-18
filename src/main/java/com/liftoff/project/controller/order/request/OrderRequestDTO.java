package com.liftoff.project.controller.order.request;

import com.liftoff.project.model.order.CustomerType;
import com.liftoff.project.model.order.Salutation;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull
    private CustomerType customerType;

    private String nip;

    private String companyName;

    @NotNull
    private Salutation salutation;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String email;

    @NotNull
    private String deliveryMethodName;

    @NotNull
    private AddressRequestDTO shippingAddress;

    @NotNull
    private AddressRequestDTO billingAddress;

    @NotNull
    private String paymentMethodName;

    @NotNull
    private Boolean termsAccepted;

    private List<OrderItemRequestDTO> orderItemRequestDTOList;

    @AssertTrue(message = "NIP and company name are required for COMPANY customer type")
    private boolean isNipAndCompanyNameValid() {
        if (customerType == CustomerType.COMPANY) {
            return nip != null && !nip.isEmpty() && companyName != null && !companyName.isEmpty();
        }
        return true;
    }

}
