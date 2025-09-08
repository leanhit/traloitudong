import { ref, reactive } from "vue";
import { defineStore } from "pinia";
import { imageApi } from '@/api/imageApi';

export const useImageStore = defineStore("imageStore", () => {
    // Sử dụng reactive để quản lý dữ liệu phức tạp như phân trang
    const imagePagination = reactive({
        content: [], // Danh sách ảnh trên trang hiện tại
        totalElements: 0, // Tổng số ảnh
        totalPages: 0, // Tổng số trang
        pageNumber: 0, // Số trang hiện tại (lưu ý: backend trả về page 0)
        pageSize: 10, // Kích thước trang
    });

    const isLoading = ref(false);
    const error = ref(null);

    // Hàm tiện ích convert URL
    function convertUrl(fileUrl: string) {
        return fileUrl.replace('http://localhost:9000', 'https://image.traloitudong.com');
    }

    async function getAllImages(page: number, size: number) {
        isLoading.value = true;
        error.value = null; // Xóa lỗi cũ
        try {
            const response = await imageApi.getAllImages(page, size);
            if (response.status === 200) {
                // Cập nhật state với dữ liệu phân trang từ backend
                // Cập nhật state với dữ liệu phân trang từ backend
                imagePagination.content = response.data.content.map(img => ({
                    ...img,
                    fileUrl: convertUrl(img.fileUrl), // Convert URL
                }));
                imagePagination.totalElements = response.data.totalElements;
                imagePagination.totalPages = response.data.totalPages;
                imagePagination.pageNumber = response.data.number;
                imagePagination.pageSize = response.data.size;
            } else {
                error.value = `Error: ${response.status}`;
            }
        } catch (err) {
            console.error('API call failed:', err);
            error.value = 'Failed to fetch images. Please try again.';
        } finally {
            isLoading.value = false;
        }
    }

    return {
        imagePagination,
        isLoading,
        error,
        getAllImages,
    };
});