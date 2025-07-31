import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import Header from './components/layout/Header';
import HeaderMobile from './components/layout/HeaderMobile';
import Footer from './components/layout/Footer';

import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import AdminDashboardPage from './pages/AdminDashboardPage';
import CustomerHomePage from './pages/CustomerHomePage';

function App() {
  return (
    <BrowserRouter>
      <div className="d-flex flex-column min-vh-100">
        {/* <Header /> */}
        <HeaderMobile />
        <main className="flex-grow-1" style={{ marginTop: '100px' }}>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/admin/dashboard" element={<AdminDashboardPage />} />
            <Route path="/home" element={<CustomerHomePage />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
