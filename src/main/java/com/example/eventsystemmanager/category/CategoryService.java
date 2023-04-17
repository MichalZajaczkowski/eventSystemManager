package com.example.eventsystemmanager.category;

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

    private static final String CATEGORYWITHIDSTATEMENT = "Category with id " + id + " does not exist.";
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
                .orElseThrow(() -> new IllegalArgumentException(CATEGORYWITHIDSTATEMENT));
    }

    public void addCategory(CategoryDto categoryDto) {
        category(categoryDto);
    }

    public void updateCategory(CategoryDto categoryDto) {
        category(categoryDto);
    }

    private void category(CategoryDto categoryDto) {
        if (categoryRepository.findByDescription(categoryDto.getDescription()).isPresent()) {
            log.info("Log: Category with description '" + categoryDto.getDescription() + "' already exists");
            throw new NonUniqueResultException("Category with description '" + categoryDto.getDescription() + "' already exists");
        }
        if(categoryRepository.findById(categoryDto.getId()).isPresent()){
            log.info("Log: Category with id '" + categoryDto.getId() + "' already exists");
            throw new IllegalArgumentException(CATEGORYWITHIDSTATEMENT);
        }
        categoryRepository.save(categoryDto.toCategory());
    }
}
