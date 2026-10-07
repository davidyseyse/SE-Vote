// src/lib/axios.ts
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE,
  timeout: 10000,
});

// 請求攔截器：自動加上 token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 回應攔截器：錯誤統一處理
api.interceptors.response.use(
  res => res,
  err => {
    const status = err.response?.status;

    if (status === 401) {
      alert('請再次確認帳號密碼');
    } else if (status === 500) {
      alert('伺服器錯誤，請稍後再試');
    }

    return Promise.reject(err); // 保留 throw 給 catch 處理邏輯
  }
);
export default api;
