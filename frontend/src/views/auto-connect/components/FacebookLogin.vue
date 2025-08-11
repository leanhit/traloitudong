<template>
    <button @click="loginWithFacebook" class="facebook-button">
        <img
            src="https://static.xx.fbcdn.net/rsrc.php/v3/yS/r/C_5f8W3w1n-.png"
            alt="Facebook logo"
            class="icon" />
        Đăng nhập với Facebook
    </button>
</template>

<script>
import { onMounted } from 'vue';

export default {
    name: 'FacebookLogin',
    setup(props, { emit }) {
        onMounted(() => {
            // Logic kiểm tra xem SDK đã được tải chưa.
            // Dù đã có trong index.html nhưng việc này giúp đảm bảo.
            if (typeof window.FB === 'undefined') {
                console.error('Facebook SDK is not loaded!');
                return;
            }
        });

        const loginWithFacebook = () => {
            if (typeof window.FB !== 'undefined') {
                window.FB.login(
                    (response) => {
                        if (response.authResponse) {
                            const { accessToken, userID } =
                                response.authResponse;
                            console.log(
                                'Đăng nhập thành công!',
                                accessToken,
                                userID
                            );

                            // Gửi dữ liệu đăng nhập lên component cha để xử lý (ví dụ: gọi API backend)
                            emit('loginSuccess', { accessToken, userID });
                        } else {
                            console.log(
                                'Người dùng đã hủy đăng nhập hoặc không cấp quyền.'
                            );
                            emit('loginFailure', 'Đăng nhập bị hủy.');
                        }
                    },
                    { scope: 'public_profile,email' }
                ); // Yêu cầu quyền truy cập email và public_profile
            }
        };

        return {
            loginWithFacebook,
        };
    },
};
</script>

<style scoped>
.facebook-button {
    display: inline-flex;
    align-items: center;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    background-color: #4267b2;
    color: white;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.facebook-button:hover {
    background-color: #365899;
}

.icon {
    width: 24px;
    height: 24px;
    margin-right: 10px;
}
</style>
