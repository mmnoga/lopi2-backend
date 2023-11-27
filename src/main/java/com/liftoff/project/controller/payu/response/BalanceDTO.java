package com.liftoff.project.controller.payu.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("total")
    private String total;

    @JsonProperty("available")
    private String available;

}