// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import ZoterDefault from '@/layouts/ZoterDefault.vue';

const routes = [
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/Login.vue'),
    },
    {
        path: '/',
        component: ZoterDefault,
        name: 'LayoutZoter',
        redirect: '/home',
        children: [
            {
                path: '',
                name: 'home',
                component: () => import('@/views/Home.vue'),
                meta: { requiresAuth: true },
            },
            {
                path: 'help',
                name: 'help',
                component: () => import('@/views/help/Index.vue'),
                meta: { requiresAuth: true },
            },
            {
                path: 'create-fb-connection',
                name: 'create-connection',
                component: () => import('@/views/create-connection/Index.vue'),
                meta: { requiresAuth: true },
            },
            {
                path: 'auto-connect',
                name: 'auto-connect',
                component: () => import('@/views/auto-connect/Index.vue'),
                meta: { requiresAuth: true },
            },
            {
                path: 'generate-embed-code',
                name: 'generate-embed-code',
                component: () =>
                    import('@/views/generate-embed-code/Index.vue'),
                meta: { requiresAuth: true },
            },
            {
                path: 'profile',
                name: 'profile',
                component: () => import('@/views/profile/Index.vue'),
                meta: { requiresAuth: true },
            },
            {
                // Route cha cho Image Manager
                path: 'image-manager',
                name: 'image-manager',
                redirect: { name: 'images' }, // Tự động chuyển hướng đến route con đầu tiên
                component: () => import('@/views/image-manager/ImageManagerLayout.vue'), // Tạo một layout cha
                meta: { requiresAuth: true },
                children: [
                    {
                        path: 'images',
                        name: 'images',
                        component: () => import('@/views/image-manager/image/Index.vue'),
                        meta: { requiresAuth: true, title: 'Image List' },
                    },
                    {
                        path: 'categories',
                        name: 'categories',
                        component: () => import('@/views/image-manager/category/Index.vue'),
                        meta: { requiresAuth: true, title: 'Category List' },
                    },
                ],
            },
            {
                // Route cha cho bot Manager
                path: 'bot-manager',
                name: 'bot-manager',
                redirect: { name: 'images' }, // Tự động chuyển hướng đến route con đầu tiên
                component: () => import('@/views/bot-manager/BotManagerLayout.vue'), // Tạo một layout cha
                meta: { requiresAuth: true },
                children: [
                    {
                        path: 'create-bot',
                        name: 'create-bot',
                        component: () => import('@/views/bot-manager/create-bot/Index.vue'),
                        meta: { requiresAuth: true, title: 'Midleware Bot List' },
                    },
                    {
                        path: 'bot-botpress',
                        name: 'bot-botpress',
                        component: () => import('@/views/bot-manager/bot-botpress/Index.vue'),
                        meta: { requiresAuth: true, title: 'Botpress Bot List' },
                    },
                ],
            },
        ],
    },
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    // Nếu đã đăng nhập mà vào lại login → chuyển về home
    if (authStore.token && to.name === 'login') {
        return next({ name: 'home' });
    }

    // Nếu chưa đăng nhập mà vào trang yêu cầu auth
    if (!authStore.token && to.meta.requiresAuth) {
        return next({ name: 'login' });
    }

    next();
});

export default router;
