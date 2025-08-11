// /src/plugins/axios.ts (hoặc .js)

import axios from 'axios';
import { useAuthStore } from '@/stores/auth'; // Giả định đường dẫn này đúng
import router from '@/router'; // Giả định đường dẫn này đúng

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_URL, // Đảm bảo VITE_API_URL được cấu hình đúng trong .env.local của Vue
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    },
});

instance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken'); // Lấy token từ localStorage
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

instance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            const authStore = useAuthStore();
            authStore.logout(); // Đăng xuất người dùng
            router.push({ name: 'login' }); // Chuyển hướng về trang login
        }
        return Promise.reject(error);
    }
);

export default instance;
