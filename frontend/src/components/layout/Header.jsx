import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';
import { NavLink } from 'react-router-dom';

const Header = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary px-3 d-none d-sm-block">
      <div className="container">
        {/* Logo */}
        <Link className="navbar-brand fs-4" to="/home">MyApp</Link>

        {/* Menu + search: gập được khi mobile */}
        <div className="collapse navbar-collapse justify-content-end" id="navbarNavAlt">
          <div className="nav-right-area d-flex flex-column align-items-end w-100">
            {/* Menu */}
            <ul className="navbar-nav flex-row flex-lg-row gap-3 mb-2 justify-content-end">
              <li className="nav-item">
                <NavLink className="nav-link" to="/home">Home</NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" to="/product">Product</NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" to="/about">About Us</NavLink>
              </li>

              <li className="nav-item">
                <NavLink className="nav-link" to="/contact">Contact</NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" to="/admin/dashboard">My Page</NavLink>
              </li>
            </ul>
            <button class="btn btn-outline-secondary d-flex align-items-center gap-2" aria-haspopup="true" aria-expanded="false">
              <span class="me-1">🔍</span>
            </button>


            {/* Search */}
            {/* <form className="d-flex w-100 justify-content-end">
              <input
                className="form-control form-control-sm"
                type="search"
                placeholder="商品を検索"
                aria-label="Search"
                style={{ maxWidth: '300px' }}
              />
            </form> */}
          </div>
        </div>
        <div>

        </div>
      </div>
    </nav>
  );
};

export default Header;
