import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, useNavigate } from 'react-router-dom';

import HeaderMobile from './components/layout/HeaderMobile';
import Footer from './components/layout/Footer';

import LoginPage from './pages/LoginPage';
import Unauthorized from './pages/Unauthorized';
import RegisterPage from './pages/RegisterPage';
import AdminHomePage from './pages/admin/AdminHomePage';
import AdminProductList from './pages/admin/ProductList';
import CustomerHomePage from './pages/CustomerHomePage';
import ProtectedRoute from './components/auth/ProtectedRoute';
import { Navigate } from 'react-router-dom';
import { isTokenExpired } from './utils/jwtUtils';  // hoặc nơi bạn để hàm này
import { refreshAccessToken } from './services/authService';

function AppWrapper() {
  // Vì useNavigate phải dùng trong BrowserRouter
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}

function App() {
  const [authChecked, setAuthChecked] = useState(false);
  const navigate = useNavigate();



useEffect(() => {
  const checkLogin = async () => {
    const hasLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
    if (!hasLoggedIn) {
      setAuthChecked(true);
      return; // không gọi refresh
    }

    // const token = localStorage.getItem('accessToken');
    // if (!token || isTokenExpired(token)) {
    //   try {
    //     // await refreshAccessToken();
    //   } catch (err) {
    //     localStorage.removeItem('isLoggedIn');
    //     setAuthChecked(true);
    //     navigate('/login');
    //     return;
    //   }
    // }

    setAuthChecked(true);
  };

  checkLogin();
}, [navigate]);



// if (!authChecked) return <Navigate to="/login" replace />;


  return (
    <div className="d-flex flex-column min-vh-100">
      <HeaderMobile />
      <main className="flex-grow-1" style={{ marginTop: '100px' }}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/unauthorized" element={<Unauthorized />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/admin/home"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <AdminHomePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/products"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <AdminProductList />
              </ProtectedRoute>
            }
          />
          <Route path="/home" element={<CustomerHomePage />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default AppWrapper;
