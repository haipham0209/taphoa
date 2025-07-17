import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../../services/authService';
import { getUserRole } from '../../utils/jwtUtils';

const LoginForm = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [remember, setRemember] = useState(false);  // trạng thái checkbox

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Gửi thêm remember nếu cần backend xử lý
      await login(email, password, remember);

      const role = getUserRole();
      if (role === 'ADMIN') navigate('/admin/dashboard');
      else navigate('/home');
    } catch (err) {
      alert('Login failed');
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-5">
          <div className="p-4 shadow w-100 max-w-[70vw] mx-auto">
            <h2 className="text-center mb-4">Login</h2>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <input
                  type="email"
                  className="form-control"
                  value={email}
                  onChange={e => setEmail(e.target.value)}
                  placeholder="Email"
                  required
                />
              </div>
              <div className="mb-3">
                <input
                  type="password"
                  className="form-control"
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                  placeholder="Password"
                  required
                />
              </div>

              {/* Checkbox nhớ đăng nhập */}
              <div className="form-check mb-3">
                <input
                  className="form-check-input"
                  type="checkbox"
                  id="checkbox_remember_account"
                  checked={remember}
                  onChange={e => setRemember(e.target.checked)}
                />
                <label className="form-check-label" htmlFor="checkbox_remember_account">
                  ログイン状態を保存する
                </label>
              </div>

              <button type="submit" className="btn btn-primary w-100 mb-3">
                Login
              </button>
            </form>

            <p className="text-center">
              Don’t have an account yet? <a href="/register">Register</a>
            </p>
            <p className="text-center">
              <a href="/forgot-password">パスワードを忘れた場合</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;
