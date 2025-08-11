import { ref, reactive } from "vue";
import { defineStore } from "pinia";
import { imageApi } from '@/api/imageApi';

export const useImageStore = defineStore("imageStore", () => {
    const image = ref(null);
    async function getAllImages(pagePagination: { page: number, size: number }) {
        try {
            const response = await imageApi.getAllImages(pagePagination);
            if (response.status == 200) {
                image.value = response.data;
            } else {
                console.log('Error:', response.status);
            }
        } catch (err) {
            console.log('Error:', err);
        }
    }

    return {
        image,
        getAllImages,
    };
});