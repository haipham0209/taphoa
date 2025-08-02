// src/services/productService.js
import axios from './axiosInstance';
const API_URL = process.env.REACT_APP_API_URL;
export const getProductList = async () => {
  try {
    const response = await axios.get(`${API_URL}/api/admin/products`); // API backend trả về danh sách sản phẩm
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách sản phẩm:', error);
    throw error;
  }
};
