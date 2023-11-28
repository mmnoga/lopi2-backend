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
public class OrderStatusDTO {

    @JsonProperty("statusCode")
    private String statusCode;

    @JsonProperty("statusDesc")
    private String statusDesc;

}