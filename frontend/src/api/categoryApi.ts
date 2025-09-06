// ✅ Correct - use the instance that has setup baseURL and interceptors
import axios from '@/plugins/axios';

export const categoryApi = {
    // READ: Get a category by its ID
    getCategoryByID(categoryId: string) {
        return axios.get(`/categories/${categoryId}`);
    },

    // UPDATE: Update a category by its ID with new parameters
    updateCategory(categoryId: string, params: any) {
        return axios.put(`/categories/${categoryId}`, params);
    },

    // DELETE: Delete a category by its ID
    deleteCategory(categoryId: string) {
        return axios.delete(`/categories/${categoryId}`);
    },

    // READ: Get all categories
    getAllCategories() {
        return axios.get(`/categories`);
    },

    // CREATE: Add a new category
    addCategory(params: any) {
        return axios.post(`/categories`, params);
    },
};