package com.example.demo.controller.admin;

import com.example.demo.dto.CategoryGroupDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.SuccessResponseDto;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

	@Autowired
	private ProductService productService;

	// 1. Lấy tất cả sản phẩm dạng 2 chiều (theo category)
	@GetMapping
	public List<CategoryGroupDto> getProductsGroupedByCategory(@RequestParam(defaultValue = "0") int page) {
	    return productService.getProductsGroupedByCategory(page);
	}
//
	
	// 2. Lấy chi tiết sản phẩm
	@GetMapping("/{productId}")
	public ProductDto getProductById(@PathVariable Long productId) {
		return productService.getProductById(productId);
	}

	// 3. Tạo mới sản phẩm
	@PostMapping
	public ProductDto createProduct(@Valid @RequestBody ProductDto dto) {
		return productService.createProduct(dto);
	}

	// 4. Cập nhật sản phẩm
	@PutMapping("/{productId}")
	public ResponseEntity<SuccessResponseDto> updateProduct(@PathVariable Long productId,
			@Valid @RequestBody ProductDto dto) {
		productService.updateProduct(productId, dto);
		return ResponseEntity.ok(new SuccessResponseDto("Product updated successfully", 200));
	}

	// 5. Xoá sản phẩm
	@DeleteMapping("/{productId}")
	public ResponseEntity<SuccessResponseDto> deleteProduct(@PathVariable Long productId) {
		productService.deleteProduct(productId);
		return ResponseEntity.ok(new SuccessResponseDto("Product deleted successfully", 200));
	}

	// 6. Tìm kiếm gợi ý (barcode hoặc tên gần giống)
	@GetMapping("/search-suggest")
	public List<ProductDto> searchSuggest(@RequestParam("query") String query) {
		return productService.searchSuggest(query);
	}

	// 7. Lấy tất cả sản phẩm theo category cụ thể
	@GetMapping("/category/{categoryId}")
	public List<ProductDto> getProductsByCategory(@PathVariable Long categoryId) {
		return productService.getProductsByCategoryId(categoryId);
	}

	// 8. Tìm kiếm theo tên sản phẩm (gần đúng hoặc chính xác)
	@GetMapping("/search")
	public List<ProductDto> searchByProductName(@RequestParam("name") String name) {
		return productService.searchByProductName(name);
	}
}
