// store/search.js (Pinia)
import { defineStore } from 'pinia';

export const useSearchStore = defineStore('search', {
    state: () => ({
        query: '',
    }),
    actions: {
        setQuery(q) {
            this.query = q;
        },
    },
});