package com.example.eventsystemmanager.category;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    @NotBlank
    private Long id;
    @NotBlank
    private String description;

    public CategoryDto(CategoryEntity categoryEntity) {
        this.id = categoryEntity.getId();
        this.description = categoryEntity.getDescription();
    }

    public CategoryEntity toCategory() {
        return new CategoryEntity(
                id,
                description
        );
    }
}