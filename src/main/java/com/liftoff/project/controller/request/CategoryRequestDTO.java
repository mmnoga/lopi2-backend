package com.liftoff.project.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CategoryRequestDTO {

    @NotNull(message = "Parent category ID cannot be null")
    private UUID parentCategoryId;
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    private String icon;
    private String imagePath;

}
