package com.example.eventsystemmanager.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto categoryMapToDto(CategoryEntity categoryEntity);
}
