// ✅ Đúng — dùng instance đã setup baseURL và interceptor
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

    getAllImages(params: any) {
        return axios.get(`/images`, params);
    },

    addImage(params: any) {
        return axios.post(`/images`, params);
    },

};
