import React from 'react';
import { Navigate } from 'react-router-dom';
import { getUserRole } from '../../utils/jwtUtils';

const ProtectedRoute = ({ children, requiredRole }) => {
  const role = getUserRole(); // hàm lấy role user từ token

  if (!role) {
    // Chưa đăng nhập, chuyển về login
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && role !== requiredRole) {
    // Không đúng role, có thể redirect về trang khác hoặc báo lỗi
    return <Navigate to="/unauthorized" replace />;
  }

  // Đủ quyền, render component
  return children;
};

export default ProtectedRoute;
