import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL;
console.log("API URL:", process.env.REACT_APP_API_URL);

export const login = async (email, password, remember) => {
  const response = await axios.post(`${API_URL}/api/login`, {
    email,
    password,
    remember
  });
  localStorage.setItem('accessToken', response.data.accessToken);
  return response.data;
};
export const refreshToken = async () => {
  try {
    const response = await axios.post(`${API_URL}/api/refresh-access-token`, null, {
      withCredentials: true,
    });

    const newAccessToken = response.data.accessToken;

    // Lưu access token mới vào localStorage
    localStorage.setItem('accessToken', newAccessToken);

    return newAccessToken;
  } catch (err) {
    console.error('Refresh token failed:', err);
    throw err;
  }
};
// Hàm đăng ký
export const register = async ({ userName, email, password }) => {
  try {
    const response = await axios.post(`${API_URL}/api/register`, {
      userName,
      email,
      password
    }, {
      headers: { 'Content-Type': 'application/json' }
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};
