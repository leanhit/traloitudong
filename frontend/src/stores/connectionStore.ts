import { ref, reactive } from 'vue';
import { defineStore } from 'pinia';
import { fbConnectionApi } from '@/api/fbConnectionApi';

export const useDataconnectionStore = defineStore('connectionStore', () => {
    const connection = ref(null);
    async function getAllConnections(pagePagination: {
        page: number;
        size: number;
    }) {
        try {
            const response = await fbConnectionApi.getAllConnections(
                pagePagination
            );
            console.log('Response:', response.data);
            if (response.status == 200) {
                connection.value = response.data;
            } else {
                console.log('Error:', response.status);
            }
        } catch (err) {
            console.log('Error:', err);
        }
    }

    return {
        connection,
        getAllConnections,
    };
});
