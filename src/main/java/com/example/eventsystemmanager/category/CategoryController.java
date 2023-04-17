package com.example.eventsystemmanager.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getCategory() {
        List<CategoryDto> result = categoryService.findAll();
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.findById(id);
        log.info("Log: Category found: " + categoryService.findById(id));
        return category != null ? ResponseEntity.ok(category): ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        log.info("Log: Category was created");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping()
    public ResponseEntity<CategoryDto> update(@RequestBody CategoryDto categoryDto) {
        categoryService.updateCategory(categoryDto);
        log.info("Log: Update category was successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
