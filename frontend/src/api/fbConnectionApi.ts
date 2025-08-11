// ✅ Đúng — dùng instance đã setup baseURL và interceptor
import axios from '@/plugins/axios';

export const fbConnectionApi = {
    getConfigByID(connectionId: string) {
        return axios.get(`/connection/facebook/${connectionId}`);
    },

    updateConfig(connectionId: string, params: any) {
        return axios.put(`/connection/facebook/${connectionId}`, params);
    },

    deleteConfig(connectionId: string) {
        return axios.delete(`/connection/facebook/${connectionId}`);
    },

    getAllConnections(params: any) {
        return axios.get(`/connection/facebook`, params);
    },

    AddConnection(params: any) {
        return axios.post(`/connection/facebook`, params);
    },

};
