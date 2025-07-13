package com.example.demo.service;

import com.example.demo.dto.CategoryGroupDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductMapper productMapper;

	private static final int CATEGORY_PAGE_SIZE = 5;
	private static final int PRODUCT_PAGE_SIZE = 10;

	//page with category
	public List<CategoryGroupDto> getProductsGroupedByCategory(int page) {
		Pageable categoryPageable = PageRequest.of(page, CATEGORY_PAGE_SIZE);
		Page<Category> categoryPage = categoryRepository.findAll(categoryPageable);

		List<CategoryGroupDto> result = new ArrayList<>();

		for (Category category : categoryPage.getContent()) {
			Pageable productPageable = PageRequest.of(0, PRODUCT_PAGE_SIZE, Sort.by("createdAt").descending());
			Page<Product> productsPage = productRepository
					.findByCategory_CategoryIdOrderByCreatedAtDesc(category.getCategoryId(), productPageable);

			List<ProductDto> productDtos = productsPage.stream().map(productMapper::toDto).collect(Collectors.toList());

			CategoryGroupDto dto = new CategoryGroupDto();
			dto.setCategoryId(category.getCategoryId());
			dto.setCategoryName(category.getCategoryName());
			dto.setProducts(productDtos);

			result.add(dto);
		}
		return result;
	}

	// 

	public ProductDto getProductById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
		return productMapper.toDto(product);
	}

	public ProductDto createProduct(ProductDto dto) {
	    // 
	    if (productRepository.existsByBarcode(dto.getBarcode())) {
	        throw new DuplicateResourceException("Barcode is existed: " + dto.getBarcode());
	    }

	    Product product = productMapper.toEntity(dto);
	    productRepository.save(product);
	    return productMapper.toDto(product);
	}

	public void updateProduct(Long id, ProductDto dto) {
	    Product product = productRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Product not found with product id : " + id));

	    // 
	    if (!product.getBarcode().equals(dto.getBarcode())
	            && productRepository.existsByBarcode(dto.getBarcode())) {
	        throw new DuplicateResourceException("Barcode is existed：　" + dto.getBarcode());
	    }

	    product.setProductName(dto.getProductName());
	    product.setCategory(new Category(dto.getCategoryId()));
	    product.setPrice(dto.getPrice());
	    product.setCostPrice(dto.getCostPrice());
	    product.setDiscountedPrice(dto.getDiscountedPrice());
	    product.setDescription(dto.getDescription());
	    product.setStockQuantity(dto.getStockQuantity());
	    product.setBarcode(dto.getBarcode());
	    product.setProductImage(dto.getProductImage());

	    productRepository.save(product);
	}


	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found with ID: " + id);
		}
		productRepository.deleteById(id);
	}

	public List<ProductDto> searchSuggest(String query) {
		List<Product> products = productRepository.findTop10ByProductNameContainingIgnoreCaseOrBarcodeContaining(query,	query);
		return products.stream().map(productMapper::toDto).collect(Collectors.toList());
	}

	public List<ProductDto> getProductsByCategoryId(Long categoryId) {
		List<Product> products = productRepository.findByCategory_CategoryId(categoryId);
		return products.stream().map(productMapper::toDto).collect(Collectors.toList());
	}

	public List<ProductDto> searchByProductName(String name) {
		List<Product> products = productRepository.findTop25ByProductNameContainingIgnoreCase(name);
		return products.stream().map(productMapper::toDto).collect(Collectors.toList());
	}
}
