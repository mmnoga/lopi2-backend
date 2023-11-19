package com.liftoff.project.controller.review.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationParameterRequestDTO {

    @Schema(defaultValue = "0")
    @Min(value = 0, message = "Page index must be greater than or equal to 0")
    private int pageIndex;

    @Schema(defaultValue = "10")
    @Min(value = 1, message = "Page size must be greater than 0")
    private int pageSize;

    @Schema(defaultValue = "true")
    private Boolean isVisible;

}