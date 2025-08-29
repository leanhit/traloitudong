// stores/facebook.js (Ví dụ sử dụng Pinia)
import { defineStore } from 'pinia';

export const useFacebookStore = defineStore('facebook', {
  state: () => ({
    userAccessToken: null,
    userID: null,
    pages: [],
  }),
  actions: {
    setFacebookData(data) {
      this.userAccessToken = data.accessToken;
      this.userID = data.userID;
    },
    setPages(pages) {
      this.pages = pages;
    },
    clearData() {
      this.userAccessToken = null;
      this.userID = null;
      this.pages = [];
    }
  },
  getters: {
    isLoggedIn: (state) => !!state.userAccessToken,
  },
});