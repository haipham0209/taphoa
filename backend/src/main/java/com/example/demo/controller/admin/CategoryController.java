package com.example.demo.controller.admin;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.SuccessResponseDto;
import com.example.demo.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public List<CategoryDto> getAllCategories() {
		return categoryService.getAllCategories();
	}

	@GetMapping("/{id}")
	public CategoryDto getCategoryById(@PathVariable Long id) {
		return categoryService.getCategoryById(id);
	}

	@PostMapping
	public CategoryDto createCategory(@Valid @RequestBody CategoryDto dto) {
		return categoryService.createCategory(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SuccessResponseDto> updateCategory(@PathVariable Long id,
			@Valid @RequestBody CategoryDto dto) {

		categoryService.updateCategory(id, dto);
		return ResponseEntity.ok(new SuccessResponseDto("Category updated successfully", 200));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponseDto> deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
		return ResponseEntity.ok(new SuccessResponseDto("Category deleted successfully", 200));
	}

}
