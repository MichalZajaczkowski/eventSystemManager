package com.example.eventsystemmanager.mapper;

import com.example.eventsystemmanager.dto.CategoryDto;
import com.example.eventsystemmanager.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto categoryMapToDto(CategoryEntity categoryEntity);
}
