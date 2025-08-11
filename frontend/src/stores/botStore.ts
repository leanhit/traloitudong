// client/src/stores/bot.ts (hoặc .js)

import { ref, reactive } from 'vue';
import { defineStore } from 'pinia';
import { botApi } from '@/api/botApi'; // Đảm bảo đường dẫn này đúng

export const useBotStore = defineStore('botStore', () => {
    // State để lưu trữ danh sách các bot của người dùng
    const bot = ref([]); // Sẽ là một mảng các đối tượng bot
    const selectedBot = ref(null); // Để lưu trữ bot đang được chọn/xem chi tiết
    const isLoading = ref(false); // Trạng thái loading cho các API calls
    const error = ref(null); // Lưu trữ lỗi nếu có

    async function getAllBots(pagePagination: { page: number; size: number }) {
        try {
            const response = await botApi.getAllBots(pagePagination);
            console.log('response======>:', response);
            if (response.status == 200) {
                bot.value = response.data;
            } else {
                console.log('Error:', response.status);
            }
        } catch (err) {
            console.log('Error:', err);
        }
    }

    /**
     * Tạo một bot cơ bản mới.
     * @param botName Tên hiển thị của bot.
     * @param botType Loại bot (ví dụ: 'welcome-bot', 'empty-bot').
     * @returns Promise<Object> Đối tượng bot đã tạo.
     */
    async function createNewBot(botName: string, botType: string) {
        isLoading.value = true;
        error.value = null;
        try {
            const response = await botApi.addBot({ botName, botType });
            if (response.status === 201 && response.data && response.data.bot) {
                // Thêm bot mới vào danh sách nếu thành công
                bot.value.push(response.data.bot);
                console.log('Bot created successfully:', response.data.bot);
                return response.data.bot;
            } else {
                const errorMessage =
                    response.data?.message || 'Failed to create bot.';
                error.value = errorMessage;
                console.error(
                    'Error creating bot:',
                    errorMessage,
                    response.status
                );
                throw new Error(errorMessage); // Ném lỗi để component có thể bắt
            }
        } catch (err: any) {
            error.value =
                err.response?.data?.message ||
                err.message ||
                'An unexpected error occurred while creating bot.';
            console.error('Error creating bot (catch block):', err);
            throw err; // Ném lỗi để component có thể bắt
        } finally {
            isLoading.value = false;
        }
    }

    /**
     * Cập nhật thông tin kết nối Facebook cho một bot.
     * @param botId ID của bot cần cập nhật.
     * @param fbPageId ID trang Facebook.
     * @param fbAccessToken Access Token trang Facebook.
     * @returns Promise<Object> Đối tượng kết nối đã cập nhật.
     */
    async function updateBotFacebookConnection(
        botId: string,
        fbPageId: string,
        fbAccessToken: string
    ) {
        isLoading.value = true;
        error.value = null;
        try {
            const response = await botApi.updateFacebookConnection(botId, {
                fbPageId,
                fbAccessToken,
            });
            if (
                response.status === 200 &&
                response.data &&
                response.data.connection
            ) {
                // Cập nhật bot trong danh sách
                const index = bot.value.findIndex((bot) => bot.botId === botId);
                if (index !== -1) {
                    bot.value[index] = {
                        ...bot.value[index],
                        ...response.data.connection,
                    };
                }
                console.log(
                    'Facebook connection updated successfully:',
                    response.data.connection
                );
                return response.data.connection;
            } else {
                const errorMessage =
                    response.data?.message ||
                    'Failed to update Facebook connection.';
                error.value = errorMessage;
                console.error(
                    'Error updating Facebook connection:',
                    errorMessage,
                    response.status
                );
                throw new Error(errorMessage);
            }
        } catch (err: any) {
            error.value =
                err.response?.data?.message ||
                err.message ||
                'An unexpected error occurred while updating Facebook connection.';
            console.error(
                'Error updating Facebook connection (catch block):',
                err
            );
            throw err;
        } finally {
            isLoading.value = false;
        }
    }

    /**
     * Lấy mã nhúng web cho một bot.
     * @param botId ID của bot.
     * @returns Promise<string> Mã nhúng HTML.
     */
    async function fetchWebEmbedCode(botId: string) {
        isLoading.value = true;
        error.value = null;
        try {
            const response = await botApi.getWebEmbedCode(botId);
            if (
                response.status === 200 &&
                response.data &&
                response.data.embedCode
            ) {
                console.log('Web embed code fetched successfully.');
                return response.data.embedCode;
            } else {
                const errorMessage =
                    response.data?.message || 'Failed to fetch web embed code.';
                error.value = errorMessage;
                console.error(
                    'Error fetching web embed code:',
                    errorMessage,
                    response.status
                );
                throw new Error(errorMessage);
            }
        } catch (err: any) {
            error.value =
                err.response?.data?.message ||
                err.message ||
                'An unexpected error occurred while fetching web embed code.';
            console.error('Error fetching web embed code (catch block):', err);
            throw err;
        } finally {
            isLoading.value = false;
        }
    }

    /**
     * Lấy danh sách các loại bot (templates) có sẵn.
     * @returns Promise<Array<Object>> Danh sách các template bot.
     */
    async function fetchBotTemplates() {
        isLoading.value = true;
        error.value = null;
        try {
            const response = await botApi.getBotTemplates();
            if (
                response.status === 200 &&
                response.data &&
                Array.isArray(response.data.templates)
            ) {
                console.log(
                    'Bot templates fetched successfully:',
                    response.data.templates
                );
                return response.data.templates;
            } else {
                const errorMessage =
                    response.data?.message || 'Failed to fetch bot templates.';
                error.value = errorMessage;
                console.error(
                    'Error fetching bot templates:',
                    errorMessage,
                    response.status
                );
                throw new Error(errorMessage);
            }
        } catch (err: any) {
            error.value =
                err.response?.data?.message ||
                err.message ||
                'An unexpected error occurred while fetching bot templates.';
            console.error('Error fetching bot templates (catch block):', err);
            throw err;
        } finally {
            isLoading.value = false;
        }
    }

    return {
        bot,
        selectedBot,
        isLoading,
        error,
        getAllBots,
        createNewBot,
        updateBotFacebookConnection,
        fetchWebEmbedCode,
        fetchBotTemplates,
        // Bạn có thể thêm các hàm khác ở đây (ví dụ: sendMessage, deleteBot)
    };
});
