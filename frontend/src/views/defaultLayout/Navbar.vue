<template>
    <nav class="navbar-custom">
        <ul class="list-inline float-right mb-0">
            <li class="list-inline-item hide-phone app-search">
                <SearchBox />
            </li>
            <li class="list-inline-item dropdown notification-list hide-phone">
                <a
                    class="nav-link dropdown-toggle arrow-none waves-effect text-white"
                    data-toggle="dropdown"
                    href="#"
                    role="button"
                    aria-haspopup="false"
                    aria-expanded="false">
                    <img
                        :src="currentFlag"
                        class="ml-2"
                        height="16"
                        alt="Language Flag" />
                </a>

                <div class="dropdown-menu dropdown-menu-right language-switch">
                    <a
                        class="dropdown-item"
                        href="#"
                        v-for="localeItem in supportedLocales"
                        :key="localeItem.value"
                        @click="changeLocale(localeItem.value)">
                        <img :src="localeItem.flag" alt="" height="16" /><span>
                            {{ localeItem.label }}
                        </span>
                    </a>
                </div>
            </li>

            <li class="list-inline-item dropdown notification-list">
                <a
                    class="nav-link dropdown-toggle arrow-none waves-effect nav-user"
                    data-toggle="dropdown"
                    href="#"
                    role="button"
                    aria-haspopup="false"
                    aria-expanded="false">
                    <img
                        src="/images/users/avatar-1.jpg"
                        alt="user"
                        class="rounded-circle" />
                </a>

                <div
                    class="dropdown-menu dropdown-menu-right profile-dropdown custom-profile-dropdown">
                    <template v-if="isAuthenticated">
                        <div class="dropdown-item noti-title">
                            <h5>{{ user?.email || 'User' }}</h5>
                        </div>
                        <router-link to="/profile" class="dropdown-item">
                            <i
                                class="mdi mdi-account-circle m-r-5 text-muted"></i>
                            {{ t('Profile') }}
                        </router-link>
                        <router-link to="/help" class="dropdown-item">
                            <i class="mdi mdi-help-circle m-r-5 text-muted"></i>
                            {{ t('Help') }}
                        </router-link>
                        <div class="dropdown-divider"></div>
                        <a
                            class="dropdown-item"
                            href="#"
                            @click.prevent="handleLogout">
                            <i class="mdi mdi-logout m-r-5 text-muted"></i>
                            Logout
                        </a>
                    </template>
                </div>
            </li>
        </ul>

        <ul class="list-inline menu-left mb-0">
            <li class="float-left">
                <button
                    class="button-menu-mobile open-left waves-light waves-effect">
                    <i class="mdi mdi-menu"></i>
                </button>
            </li>
        </ul>

        <div class="clearfix"></div>
    </nav>
</template>

<script lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';
import { supportedLocales, currentLanguage } from '@/until/constant'; // Đảm bảo đường dẫn đúng
import { useAuthStore } from '@/stores/auth'; // Store xác thực
import { useSearchStore } from '@/stores/search';
import SearchBox from './SearchBox.vue';

export default {
    components: {
        SearchBox,
    },
    setup() {
        const { t, locale } = useI18n();
        const router = useRouter();

        // Auth store
        const authStore = useAuthStore();
        const { token, user } = storeToRefs(authStore);
        const isAuthenticated = computed(() => !!token.value);
        // Locale & Currency
        const currentLocale = ref(
            localStorage.getItem('restaurentLocale') || currentLanguage
        );
        if (currentLocale.value === null) {
            currentLocale.value = 'en'; // Giá trị mặc định nếu không tìm thấy
        }

        const currentFlag = ref('');
        const setFlag = () => {
            const found = supportedLocales.find(
                (lo) => lo.value === currentLocale.value
            );
            currentFlag.value = found ? found.flag : supportedLocales[0].flag;
        };
        setFlag();

        const changeLocale = (newLocale: string) => {
            localStorage.setItem('restaurentLocale', newLocale);
            currentLocale.value = newLocale; // Cập nhật biến reactive
            locale.value = newLocale; // Cập nhật locale của vue-i18n
            setFlag(); // Cập nhật cờ ngay lập tức
            location.reload(); // Tải lại trang để áp dụng ngôn ngữ mới (như trong code gốc của bạn)
        };

        // Logout
        function handleLogout() {
            authStore.logout();
            router.push('/login');
        }

        onMounted(() => {
            // Script Bootstrap/JQuery cho dropdown được thêm vào đây
            const recaptchaScript = document.createElement('script');
            recaptchaScript.setAttribute('src', '/js/app.js'); // Đảm bảo file này chứa logic dropdown của Bootstrap/JQuery
            document.head.appendChild(recaptchaScript);
        });

        return {
            t,
            isAuthenticated,
            handleLogout,
            changeLocale,
            currentLocale,
            currentFlag,
            supportedLocales,
            user,
        };
    },
};
</script>

<style scoped>
/* Thêm một class riêng để dễ quản lý và tránh ghi đè các dropdown khác */
.profile-dropdown.custom-profile-dropdown {
    min-width: 270px; /* Chiều rộng mặc định của Bootstrap dropdown là 160px. 160 * 1.5 = 240px. */
    /* Một số phiên bản Bootstrap mặc định là 180px, khi đó 180 * 1.5 = 270px. */
    /* Bạn nên kiểm tra kích thước mặc định của dropdown-menu trong dự án của mình */
    /* để đưa ra con số chính xác. Ví dụ, tôi dùng 270px. */
}
</style>
