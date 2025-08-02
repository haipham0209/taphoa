import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { LayoutDashboard, Users, Settings, LogOut } from 'lucide-react';
import { logout } from '../../services/authService'; // đường dẫn phù hợp với dự án của bạn

const menuItems = [
  { label: 'Dashboard', icon: <LayoutDashboard size={20} />, to: '/admin/dashboard' },
  { label: 'Quản lý người dùng', icon: <Users size={20} />, to: '/admin/users' },
  { label: 'Product', icon: <Users size={20} />, to: '/admin/products' },
  { label: 'Cài đặt hệ thống', icon: <Settings size={20} />, to: '/admin/settings' },
  { label: 'Log Out', icon: <LogOut size={20} />, action: 'logout' }, // 👈 đổi thành action
];

export default function AdminMenu() {
  const [showConfirm, setShowConfirm] = useState(false);
  const navigate = useNavigate();

  const handleMenuClick = async (item) => {
    if (item.action === 'logout') {
      setShowConfirm(true);
    }
  };

  const handleLogoutConfirm = async () => {
    setShowConfirm(false);
    await logout();
    navigate('/login');
  };

  return (
    <div className="w-64 bg-white shadow-lg p-4 h-full relative">
      <h2 className="text-xl font-semibold mb-4">Admin Menu</h2>
      <ul className="space-y-2">
        {menuItems.map((item) => (
          <li key={item.label}>
            {item.to ? (
              <Link
                to={item.to}
                className="flex items-center space-x-2 text-gray-700 hover:text-blue-600 p-2 rounded hover:bg-gray-100 transition"
              >
                {item.icon}
                <span>{item.label}</span>
              </Link>
            ) : (
              <button
                onClick={() => handleMenuClick(item)}
                className="w-full text-left flex items-center space-x-2 text-gray-700 hover:text-blue-600 p-2 rounded hover:bg-gray-100 transition"
              >
                {item.icon}
                <span>{item.label}</span>
              </button>
            )}
          </li>
        ))}
      </ul>

      {/* Dialog xác nhận đăng xuất */}
      {showConfirm && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
          <div className="bg-white rounded shadow p-6">
            <p className="text-lg">Are you sure to log out?</p>
            <div className="flex justify-end mt-4 space-x-3">
              <button
                onClick={() => setShowConfirm(false)}
                className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
              >
                Cancel
              </button>
              <button
                onClick={handleLogoutConfirm}
                className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
              >
                Log Out
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
