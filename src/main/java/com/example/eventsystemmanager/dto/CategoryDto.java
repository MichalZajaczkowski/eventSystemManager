package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.entity.CategoryEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class CategoryDto {

    @NotBlank
    private Long id;
    @NotBlank
    private String description;

    public CategoryDto(CategoryEntity category) {
        this.id = category.getId();
        this.description = category.getDescription();
    }

    public boolean hasDescription() {
        return description != null;
    }

    public CategoryEntity toCategory() {
        CategoryEntity category = new CategoryEntity();
        category.setId(id);// tu musi iść id
        category.setDescription(description);
        return category;
    }
}