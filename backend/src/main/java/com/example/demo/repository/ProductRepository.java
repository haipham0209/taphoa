package com.example.demo.repository;

import com.example.demo.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Page<Product> findByCategory_CategoryIdOrderByCreatedAtDesc(Long categoryId, Pageable pageable);


    // Tìm top 10 sản phẩm theo category (dùng cho phân trang hiển thị)
    List<Product> findTop10ByCategory_CategoryIdOrderByCreatedAtDesc(Integer categoryId);

    // Tìm tất cả theo category (không giới hạn số lượng, dùng cho /category/{id})
    List<Product> findByCategory_CategoryId(Integer categoryId);

    // Tìm sản phẩm theo barcode hoặc tên gần giống (suggest)
    List<Product> findByBarcodeContainingOrProductNameContainingIgnoreCase(String barcode, String productName);

    // Tìm theo tên chính xác hoặc gần đúng (search)
    List<Product> findByProductNameContainingIgnoreCase(String name);
}

