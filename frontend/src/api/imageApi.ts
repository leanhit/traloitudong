// src/api/image.ts
import axios from '@/plugins/axios';

export const imageApi = {
    getImageByID(imageId: string) {
        return axios.get(`/images/${imageId}`);
    },

    deleteImage(imageId: string) {
        return axios.delete(`/images/${imageId}`);
    },

    // Sửa đổi phương thức getAllImages để nhận các tham số phân trang rõ ràng
    getAllImages(page: number = 0, size: number = 10) {
        return axios.get('/images', {
            params: {
                page,
                size,
            },
        });
    },

    addImage1(params: any) {
        return axios.post(`/images`, params);
    },

    updateImage1(imageId: string, params: any) {
        return axios.put(`/images/${imageId}`, params);
    },

    updateImage(imageId: string, formData: FormData) {
        return axios.put(`/images/${imageId}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
    },

    addImage(formData: FormData) {
        return axios.post(`/images`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
    },

};