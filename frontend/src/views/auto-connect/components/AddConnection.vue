<template>
    <div>
        <h1>Chào mừng đến với ứng dụng của tôi!</h1>
        <FacebookLogin
            @loginSuccess="handleLoginSuccess"
            @loginFailure="handleLoginFailure" />
    </div>
</template>

<script>
import FacebookLogin from './FacebookLogin.vue';

export default {
    name: 'App',
    components: {
        FacebookLogin,
    },
    methods: {
        handleLoginSuccess(data) {
            console.log('Dữ liệu từ Facebook:', data);
            // Gửi data.accessToken và data.userID đến API backend của bạn
        },
        handleLoginFailure(error) {
            console.error('Lỗi đăng nhập:', error);
        },
    },
};
</script>
