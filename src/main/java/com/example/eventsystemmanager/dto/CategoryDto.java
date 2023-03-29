package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.entity.CategoryEntity;
import com.example.eventsystemmanager.entity.UserAddressEntity;
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