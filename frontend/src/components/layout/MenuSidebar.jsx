// src/layouts/MenuSidebar.jsx
import React from 'react';
import { Outlet } from 'react-router-dom';
import AdminMenu from './AdminMenu';
import './menu-side-bar.css';

export default function MenuSidebar() {
return (
<div className="flex flex-row mr-10" style={{ height: '100vh' }}>
  {/* Menu bên trái */}
  <AdminMenu />

  {/* Nội dung chính bên phải, chiếm phần còn lại */}
  <div className="custom-outlet flex-grow p-6 overflow-y-auto">
    <Outlet />
  </div>
</div>

);

}
