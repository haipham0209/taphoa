package com.example.demo.service;

import com.example.demo.dto.CategoryDto;
import com.example.demo.entity.Category;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;

	public List<CategoryDto> getAllCategories() {
		return categoryRepo.findAll().stream().map(cat -> new CategoryDto(cat.getCategoryId(), cat.getCategoryName()))
				.collect(Collectors.toList());
	}

	public CategoryDto getCategoryById(Long id) {
		Category cat = categoryRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
		return new CategoryDto(cat.getCategoryId(), cat.getCategoryName());
	}

	public CategoryDto createCategory(CategoryDto dto) {
		if (categoryRepo.existsByCategoryName(dto.getCategoryName())) {
			throw new DuplicateResourceException("Category name already exists: " + dto.getCategoryName());
		}
		Category cat = new Category();
		cat.setCategoryName(dto.getCategoryName());
		categoryRepo.save(cat);
		return new CategoryDto(cat.getCategoryId(), cat.getCategoryName());
	}

	public void updateCategory(Long id, CategoryDto dto) {
		Category cat = categoryRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
		cat.setCategoryName(dto.getCategoryName());
		categoryRepo.save(cat);
	}

	public void deleteCategory(Long id) {
		if (!categoryRepo.existsById(id)) {
			throw new ResourceNotFoundException("Category not found with ID: " + id);
		}
		categoryRepo.deleteById(id);
	}
}
