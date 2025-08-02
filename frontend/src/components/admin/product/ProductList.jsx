// src/components/admin/ProductList.jsx
import React, { useEffect, useState } from 'react';
import { getProductList } from '../../../services/productService';

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getProductList();
        setProducts(data);
      } catch (err) {
        console.error('Không thể tải danh sách sản phẩm', err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  if (loading) return <p>Đang tải sản phẩm...</p>;

  return (
    <div>
      <h2>Danh sách sản phẩm</h2>
      <table border="1" cellPadding="8" cellSpacing="0">
        <thead>
          <tr>
            <th>ID</th>
            <th>Tên sản phẩm</th>
            <th>Giá</th>
            <th>Số lượng</th>
          </tr>
        </thead>
        <tbody>
          {products.length > 0 ? (
            products.map((product) => (
              <tr key={product.product_id}>
                <td>{product.product_id}</td>
                <td>{product.product_name}</td>
                <td>{product.price}</td>
                <td>{product.quantity || '-'}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4">Không có sản phẩm nào.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ProductList;
