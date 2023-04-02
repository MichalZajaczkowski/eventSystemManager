package com.example.eventsystemmanager.service;

import com.example.eventsystemmanager.dto.CategoryDto;
import com.example.eventsystemmanager.dto.UserAddressDto;
import com.example.eventsystemmanager.entity.CategoryEntity;
import com.example.eventsystemmanager.mapper.CategoryMapper;
import com.example.eventsystemmanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryService {

    private static final String CATEGOTYWITHIDSTATEMENT = "Category with id " + id + " does not exist.";
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private CategoryDto toDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .description(categoryEntity.getDescription())
                .build();

    }

    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::categoryMapToDto)
                .orElseThrow(() -> new IllegalArgumentException(CATEGOTYWITHIDSTATEMENT));
    }

    public void addCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByDescription(categoryDto.getDescription()).isPresent()) {
            log.info("Log: Category with description '" + categoryDto.getDescription() + "' already exists");
            throw new NonUniqueResultException("Category with description '" + categoryDto.getDescription() + "' already exists");
        }
        CategoryEntity categoryEntity = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new IllegalArgumentException(CATEGOTYWITHIDSTATEMENT));
        categoryRepository.save(categoryDto.toCategory());
    }

    public void updateCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByDescription(categoryDto.getDescription()).isPresent()) {
            throw new NonUniqueResultException("Category with description '" + categoryDto.getDescription() + "' already exists");
        }
        CategoryEntity categoryEntity = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new IllegalArgumentException(CATEGOTYWITHIDSTATEMENT));

        categoryEntity.setDescription(categoryDto.getDescription());
        categoryRepository.save(categoryDto.toCategory());
    }
}
