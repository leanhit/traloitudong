// src/api/image.ts
import axios from '@/plugins/axios';

export const imageApi = {
    getImageByID(imageId: string) {
        return axios.get(`/images/${imageId}`);
    },

    updateImage(imageId: string, params: any) {
        return axios.put(`/images/${imageId}`, params);
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

    addImage(params: any) {
        return axios.post(`/images`, params);
    },

};