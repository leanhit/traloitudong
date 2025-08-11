import { defineStore } from 'pinia';
import axios from '@/plugins/axios'; // dùng instance đã tạo
import router from '@/router';

interface User {
  id: number;
  email: string;
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null as string | null,
    user: null as User | null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
  },

  actions: {
    initialize() {
      this.token = localStorage.getItem('accessToken');
      const user = localStorage.getItem('user');
      if (user) this.user = JSON.parse(user);
    },

    login(token: string, user: User) {
      this.token = token;
      this.user = user;

      localStorage.setItem('accessToken', token);
      localStorage.setItem('user', JSON.stringify(user));
    },

    logout() {
      this.token = null;
      this.user = null;

      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');

      router.push({ name: 'login' });
    },

    async fetchUser() {
      if (!this.token) return;

      try {
        const response = await axios.get('/api/me');
        const userData = response.data;
        this.user = userData;

        localStorage.setItem('user', JSON.stringify(userData));
      } catch (error) {
        console.error('Error fetching user:', error);
        this.logout();
      }
    },

    updateUserProfile(updates: Partial<User>) {
      if (!this.user) return;
      this.user = { ...this.user, ...updates };
      localStorage.setItem('user', JSON.stringify(this.user));
    },
  },
});
