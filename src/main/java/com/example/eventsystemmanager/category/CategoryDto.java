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

    public CategoryEntity toCategory() {
        CategoryEntity category = new CategoryEntity();
        category.setId(id);// tu musi iść id
        category.setDescription(description);
        return category;
    }
}