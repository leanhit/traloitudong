// /src/api/botApi.ts

// ✅ Đúng — dùng instance đã setup baseURL và interceptor
import axios from '@/plugins/axios';
import router from '@/router'; // Giả định bạn đã import Vue Router
import { AxiosError } from 'axios';

// Hàm xử lý lỗi chung cho các request
const handleApiError = (error: AxiosError) => {
    if (error.response && error.response.status === 401) {
        console.error('Lỗi xác thực: Token không hợp lệ hoặc đã hết hạn.');
        // Chuyển hướng người dùng đến trang đăng nhập
        router.push('/login');
    }
    // Ném lại lỗi để các component gọi hàm này có thể xử lý
    throw error;
};

export const botApi = {
    /**
     * Lấy thông tin chi tiết của một bot bằng ID.
     * Maps to: GET /api/bots/:botId
     * @param botId ID của bot
     */
    getBotByID(botId: string) {
        // Gọi endpoint nghiệp vụ chính của bạn
        return axios.get(`/bots/${botId}`).catch(handleApiError);
    },

    /**
     * Cập nhật thông tin của một bot.
     * Maps to: PUT /api/bots/:botId
     * @param botId ID của bot
     * @param params Dữ liệu cập nhật
     */
    updateBot(botId: string, params: any) {
        // Gọi endpoint nghiệp vụ chính của bạn
        return axios.put(`/bots/${botId}`, params).catch(handleApiError);
    },

    /**
     * Xóa một bot bằng ID.
     * Maps to: DELETE /api/bots/:botId
     * @param botId ID của bot
     */
    deleteBot(botId: string) {
        // Gọi endpoint nghiệp vụ chính của bạn
        return axios.delete(`/bots/${botId}`).catch(handleApiError);
    },

    /**
     * Lấy danh sách tất cả các bot của người dùng hiện tại.
     * Maps to: GET /api/bots
     * @param params Các tham số query (ví dụ: phân trang, lọc)
     */
    getAllBots(params: any = {}) {
        // ✅ Đã sửa: Gọi endpoint nghiệp vụ chính, không gọi proxy
        return axios.get(`/bots`, { params }).catch(handleApiError);
    },

    /**
     * Thêm một bot cơ bản mới.
     * Maps to: POST /api/bots
     * @param params Dữ liệu bot (botName, botType)
     */
    addBot(params: { botName: string; botType?: string }) {
        // ✅ Đã sửa: Gọi endpoint nghiệp vụ chính, không gọi proxy
        return axios.post(`/bots`, params).catch(handleApiError);
    },

    /**
     * Cập nhật thông tin kết nối Facebook cho một bot.
     * Maps to: PUT /api/bots/:botId/facebook
     * @param botId ID của bot cần cập nhật
     * @param params Dữ liệu kết nối Facebook (fbPageId, fbAccessToken)
     */
    updateFacebookConnection(
        botId: string,
        params: { fbPageId: string; fbAccessToken: string }
    ) {
        // ✅ Đã sửa: Gọi endpoint nghiệp vụ chính
        return axios.put(`/bots/${botId}/facebook`, params).catch(handleApiError);
    },

    /**
     * Lấy mã nhúng web cho một bot.
     * Maps to: GET /api/bots/:botId/embed-code
     * @param botId ID của bot
     */
    getWebEmbedCode(botId: string) {
        // ✅ Đã sửa: Gọi endpoint nghiệp vụ chính
        return axios.get(`/bots/${botId}/embed-code`).catch(handleApiError);
    },

    /**
     * Gửi tin nhắn đến một bot Botpress.
     * Maps to: POST /api/bots/send-message
     * @param params Dữ liệu tin nhắn (botId, userId, message)
     */
    sendMessage(params: { botId: string; userId: string; message: string }) {
        // ✅ Đã sửa: Gọi endpoint nghiệp vụ chính
        return axios.post(`/bots/send-message`, params).catch(handleApiError);
    },

    /**
     * Lấy danh sách các loại bot (templates) có sẵn.
     * Maps to: GET /api/bots/templates
     */
    getBotTemplates() {
        // ✅ Đã sửa: Gọi endpoint nghiệp vụ chính
        return axios.get(`/bots/templates`).catch(handleApiError);
    },
};
