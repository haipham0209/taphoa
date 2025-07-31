import React, { useEffect } from 'react';
import { Link, NavLink } from 'react-router-dom';
import './HeaderMobile.css';

const HeaderMobile = () => {
  useEffect(() => {
    const toggler = document.querySelector(".custom-toggler");
    const menuIcon = toggler?.querySelector(".menu-icon");
    const offcanvas = document.getElementById("mobileMenu");

    if (!toggler || !menuIcon || !offcanvas) return;

    const showHandler = () => menuIcon.classList.add("active");
    const hideHandler = () => menuIcon.classList.remove("active");

    offcanvas.addEventListener("show.bs.offcanvas", showHandler);
    offcanvas.addEventListener("hide.bs.offcanvas", hideHandler);

    // Cleanup khi component unmount
    return () => {
      offcanvas.removeEventListener("show.bs.offcanvas", showHandler);
      offcanvas.removeEventListener("hide.bs.offcanvas", hideHandler);
    };
  }, []);

  return (
    <>
      {/* Navbar - Chỉ hiển thị trên điện thoại */}
      <nav className="navbar navbar-dark bg-primary px-3 d-flex d-md-none">
        <div className="container-fluid">
          {/* Nút Toggle Offcanvas */}
          <button
            className="navbar-toggler border-0 custom-toggler"
            type="button"
            data-bs-toggle="offcanvas"
            data-bs-target="#mobileMenu"
            aria-controls="mobileMenu"
            aria-label="Toggle navigation"
          >
            <div className="menu-icon">
              <span className="bar"></span>
              <span className="bar"></span>
              <span className="bar"></span>
            </div>
          </button>


          {/* Logo */}
          <a className="navbar-brand ms-2" href="/home">
            <img
              src="../public/logo192.png"
              alt="logo"
              style={{ height: "40px" }}
            />
          </a>
        </div>
      </nav>

      {/* Offcanvas menu (trượt từ trái) */}
      <div
        className="offcanvas offcanvas-start"
        tabIndex="-1"
        id="mobileMenu"
        aria-labelledby="mobileMenuLabel"
      >
        <div className="offcanvas-header">
          <h5 className="offcanvas-title" id="mobileMenuLabel">メニュー</h5>
          <button
            className="navbar-toggler border-0 custom-toggler"
            type="button"
            data-bs-toggle="offcanvas"
            data-bs-target="#mobileMenu"
            aria-controls="mobileMenu"
            aria-label="Toggle navigation"
          >
            <div className="menu-icon">
              <span className="bar bar1"></span>
              <span className="bar bar2"></span>
              <span className="bar bar3"></span>
            </div>
          </button>
        </div>

        <div className="offcanvas-body">
          <ul className="navbar-nav">
            <li className="nav-item">
              <a className="nav-link" href="/home">Home</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/product">Product</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/home">ホームページ</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="/about-us">About Us</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="">My Page</a>
            </li>

            <li className="nav-item mt-3 fw-bold custom-support">Support</li>
            <li className="nav">
              <i className="fa fa-phone me-2"></i>
              <a className="nav-link custom-support-inline" href="tel:08012345678">08012345678</a>
            </li>
            <li className="nav">
              <i className="fa fa-envelope me-2"></i>
              <a className="nav-link custom-support-inline" href="mailto:2230291@ecc.ac.jp">2230291@ecc.ac.jp</a>
            </li>
            <li className="nav">
              <i className="fa fa-map-marker me-2"></i>
              <a className="nav-link custom-support-inline" target="_blank" href="#">Canh Vinh Van Canh Binh Dinh</a>
            </li>
            <Link
              to="/login"
              className="btn btn-success"
              onClick={() => {
                const offcanvas = document.getElementById('mobileMenu');
                if (offcanvas && offcanvas.classList.contains('show')) {
                  const bsOffcanvas = window.bootstrap.Offcanvas.getInstance(offcanvas);
                  bsOffcanvas?.hide();
                }
              }}
            >
              Login
            </Link>


          </ul>

          {/* Search */}
          <div className="mt-3">

          </div>
        </div>
      </div>
    </>
  );
};

export default HeaderMobile;
