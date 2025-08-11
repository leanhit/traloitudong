// ✅ Đúng — dùng instance đã setup baseURL và interceptor
import axios from '@/plugins/axios';

export const usersApi = {
    login(params: any) {
        return axios.post('/auth/login', params);
    },

    register(params: any) {
        return axios.post('/auth/register', params);
    },

    checkUsername(username: string) {
        return axios.get(`/auth/check-username`, {
            params: { username },
        });
    },

    checkEmail(email: string) {
        return axios.get(`/auth/check-email`, {
            params: { email },
        });
    },

    getProfile() {
        return axios.get(`/auth/profile`);
    },

    updateProfile(params: any) {
        return axios.put(`/auth/profile`, params);
    },

    updateAvatar(formData: FormData) {
        // for (const [key, value] of formData.entries()) {
        //     console.log(`FormData entry: ${key}`, value);
        // }
        return axios.put('/auth/profile/avatar', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
    },
};
