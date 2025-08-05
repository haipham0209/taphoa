import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';

import {
  LayoutDashboard,
  Users,
  Settings,
  LogOut,
  Boxes,
  ChevronDown,
  ChevronUp,
  TrendingUp,
  Tag,
  Box
} from 'lucide-react';

import { logout } from '../../services/authService';
import './menu-side-bar.css';

// menuItems phải khai báo trước
const menuItems = [
  { label: 'Home', icon: <LayoutDashboard size={25} />, to: '/admin/home' },
  { label: 'Custommers', icon: <Users size={25} />, to: '/admin/users' },
  {
    label: 'Products',
    icon: <Box size={25} />,
    subItems: [
      { label: 'All Products', icon: <Boxes size={25} />, to: '/admin/products' },
      { label: 'Sales', icon: <Tag size={25} />, to: '/admin/products/sales' },
      { label: 'On Trend', icon: <TrendingUp size={25} />, to: '/admin/products/trending' },
    ],
  },
  { label: 'System setting', icon: <Settings size={25} />, to: '/admin/settings' },
  { label: 'Log Out', icon: <LogOut size={25} />, action: 'logout' },
];

// Sau khi menuItems được định nghĩa, mới tách menu
const bottomMenuLabels = ['System setting', 'Log Out'];
const bottomMenuItems = menuItems.filter(item => bottomMenuLabels.includes(item.label));
const mainMenuItems = menuItems.filter(item => !bottomMenuLabels.includes(item.label));


export default function AdminMenu() {
  const [showConfirm, setShowConfirm] = useState(false);
  const [expandedMenus, setExpandedMenus] = useState({});
  const navigate = useNavigate();

  const location = useLocation();
  const currentPath = location.pathname;

  // Thêm useEffect này để khởi tạo expandedMenus khi currentPath thay đổi
  useEffect(() => {
    const newExpandedMenus = {};
    mainMenuItems.forEach(item => {
      if (item.subItems) {
        // Mở menu con nếu currentPath khớp với sub item
        const shouldExpand = item.subItems.some(sub => currentPath.startsWith(sub.to));
        if (shouldExpand) {
          newExpandedMenus[item.label] = true;
        }
      }
    });
    setExpandedMenus(newExpandedMenus);
  }, [currentPath]);

  // Các hàm và phần return vẫn giữ nguyên
  const toggleExpand = (label) => {
    setExpandedMenus((prev) => ({
      ...prev,
      [label]: !prev[label],
    }));
  };

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




  const logoutItem = menuItems.find(item => item.action === 'logout');
  const menuItemsWithoutLogout = menuItems.filter(item => item.action !== 'logout');


  return (

    <div className="w-64 h-screen bg-white shadow-lg flex flex-col custom-home-menu">
      {/* <div className="w-64 h-full flex flex-col bg-white shadow-lg p-4 custom-home-menu"> */}

      {/* Top menu items */}
      <div>
        <ul className="list-none hover:bg-white transition-colors duration-200">
          {mainMenuItems.map((item) => {
            const isActiveParent = item.to && currentPath.startsWith(item.to) && !item.subItems;
            const hasActiveSub = item.subItems && item.subItems.some((sub) => currentPath.startsWith(sub.to));

            return (
              <li key={item.label}>
                {item.subItems ? (
                  <>
                    <div
                      className={`custom-collapse-button flex items-center justify-between cursor-pointer px-4 py-2 transition w-full ${hasActiveSub ? 'active' : ''}`}
                      onClick={() => toggleExpand(item.label)}
                      role="menuitem"
                      tabIndex={0}
                      onKeyDown={(e) => {
                        if (e.key === 'Enter' || e.key === ' ') toggleExpand(item.label);
                      }}
                    >
                      <div className="flex items-center gap-2">
                        {item.icon}
                        <span className="custom-label">{item.label}</span>
                      </div>
                      <div className="ml-3">
                        {expandedMenus[item.label] ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                      </div>
                    </div>

                    {(expandedMenus[item.label] || (hasActiveSub && !expandedMenus.hasOwnProperty(item.label))) && (
                      <ul className="custom-sub-ul ml-6 mt-1 sub-list-none">
                        {item.subItems.map((sub) => {
                          const isActiveSub = currentPath === sub.to;
                          return (
                            <li className={`custom-sub-li ${isActiveSub ? 'active' : ''}`} key={sub.label}>
                              <Link
                                to={sub.to}
                                className={`flex items-center gap-2 px-2 py-1 hover:bg-gray-200`}
                              >
                                {sub.icon}
                                <span className="custom-label">{sub.label}</span>
                              </Link>
                            </li>
                          );
                        })}
                      </ul>
                    )}
                  </>
                ) : (
                  <Link
                    to={item.to}
                    className={`custom-collapse-button flex items-center space-x-2 p-2 transition px-4 hover:bg-white hover:text-black ${isActiveParent ? 'bg-white text-black' : ''}`}
                  >
                    {item.icon}
                    <span className="custom-label">{item.label}</span>
                  </Link>
                )}
              </li>
            );
          })}
        </ul>
      </div>

      {/* Bottom menu items: System setting & Logout */}
      <div className="mt-auto py-4 border-t">
        {bottomMenuItems.map((item) => {
          return item.to ? (
            <Link
              key={item.label}
              to={item.to}
              className="custom-bottom-nav-menu flex items-center gap-2 text-gray-700 hover:text-blue-600 p-2 rounded hover:bg-gray-100 transition w-full justify-start"
            >
              {item.icon}
              <span>{item.label}</span>
            </Link>
          ) : (
            <div
              key={item.label}
              onClick={() => handleMenuClick(item)}
              role="button"
              tabIndex={0}
              onKeyDown={(e) => {
                if (e.key === 'Enter' || e.key === ' ') handleMenuClick(item);
              }}
              className="custom-bottom-nav-menu flex items-center gap-2 text-gray-700 hover:text-blue-600 p-2 rounded hover:bg-gray-100 transition w-full justify-start cursor-pointer"
            >
              {item.icon}
              <span>{item.label}</span>
            </div>
          );
        })}
      </div>






      {/* {showConfirm && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
          <div className="absolute inset-0 bg-black bg-opacity-40" />
          <div className="z-50 bg-white rounded shadow-lg p-6 w-[90%] max-w-sm mx-auto">
            <p className="text-lg font-medium">Are you sure you want to log out?</p>
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
      )} */}
    </div>
  );
}
