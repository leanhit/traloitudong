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

    addConnection(params: any) {
        return axios.post(`/connection/facebook`, params);
    },

    // API mới để thêm nhiều kết nối
    addConnections(params: any) {
        // Gửi một mảng các đối tượng trang đến server
        return axios.post(`/connection/facebook/auto-connect`, params );
    },

    // Hàm mới để đăng ký webhook cho một trang
    subscribePageToWebhook: (pageId, pageAccessToken) => {
        return axios.post(
            `https://graph.facebook.com/v18.0/${pageId}/subscribed_apps`,
            null, // body is null for POST with params
            {
                params: {
                    access_token: pageAccessToken,
                    subscribed_fields: 'messages,messaging_postbacks',
                },
            }
        );
    },

    // Hàm mới để hủy đăng ký webhook cho một trang
    unsubscribePageFromWebhook: (pageId, pageAccessToken) => {
        return axios.delete(
            `https://graph.facebook.com/v18.0/${pageId}/subscribed_apps`,
            {
                params: {
                    access_token: pageAccessToken,
                },
            }
        );
    },
};
