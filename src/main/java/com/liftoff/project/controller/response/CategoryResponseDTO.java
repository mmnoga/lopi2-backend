package com.liftoff.project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CategoryResponseDTO {
    private UUID uId;
    private final String name;
    private final String description;
    private final Set<CategoryResponseDTO> subcategories;

    public CategoryResponseDTO(UUID uId, String name, String description) {
        this.uId = uId;
        this.name = name;
        this.description = description;
        this.subcategories = new HashSet<>();
    }

    public Set<CategoryResponseDTO> getSubcategories() {
        return subcategories;
    }

}
