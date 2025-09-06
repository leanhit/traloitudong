import { ref, reactive } from "vue";
import { defineStore } from "pinia";
import { categoryApi } from '@/api/categoryApi';

export const useCategoryStore = defineStore("categoryStore", () => {
    const categories = ref([]); // Đổi tên biến để phản ánh đúng dữ liệu là danh sách
    const loading = ref(false);
    const error = ref(null);

    async function getAllCategories() {
        loading.value = true;
        error.value = null; // Reset lỗi trước khi gọi API
        try {
            const response = await categoryApi.getAllCategories();
            if (response.status === 200) {
                categories.value = response.data; // Cập nhật danh sách
            } else {
                error.value = 'Failed to fetch categories. Status: ' + response.status;
                console.error('Error:', response.status);
            }
        } catch (err) {
            error.value = 'An error occurred while fetching categories.';
            console.error('Error:', err);
        } finally {
            loading.value = false; // Tắt trạng thái loading bất kể thành công hay thất bại
        }
    }

    return {
        categories,
        loading,
        error,
        getAllCategories,
    };
});