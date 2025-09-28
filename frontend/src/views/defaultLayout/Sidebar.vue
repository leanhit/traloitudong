<template>
    <div class="left side-menu">
        <button
            type="button"
            class="button-menu-mobile button-menu-mobile-topbar open-left waves-effect">
            <i class="ion-close"></i>
        </button>

        <div class="topbar-left navbar navbar-light position-relative">
            <div class="container-fluid">
                <router-link to="/">
                    <a class="navbar-brand ml-auto m-auto">
                        <img
                            src="@/assets/botpress-logo.png"
                            alt="Botpress Logo"
                            width="130"
                            height="30" />
                    </a>
                </router-link>
            </div>
        </div>

        <div class="sidebar-inner niceScrollleft">
            <div id="sidebar-menu">
                <ul>
                    <li class="menu-title">
                        <strong>{{ t('Tool') }}</strong>
                    </li>         
                    <li class="has_sub">
                        <router-link to="/auto-connect">
                            <i class="mdi mdi-link-variant"></i>
                            <span>
                                <strong>{{ t('Connection Manager') }}</strong>
                            </span>
                            <span class="float-right"></span>
                        </router-link>
                    </li>
                    <li class="has_sub">
                        <router-link to="/generate-embed-code">
                            <i class="mdi mdi-code-tags"></i>
                            <span>
                                <strong>{{ t('Generate Embed Code') }}</strong>
                            </span>
                            <span class="float-right"></span>
                        </router-link>
                    </li>
                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect">
                            <i class="mdi mdi-image-multiple"></i> <span>{{ t('Image Manager') }}</span>
                            <span class="menu-arrow float-right"><i class="mdi mdi-chevron-right"></i></span>
                        </a>
                        <ul class="list-unstyled">
                            <li class="ml-5">
                                <router-link to="/image-manager/images">
                                    <i class="mdi mdi-image"></i> 
                                    <span>{{ t('Images') }}</span>
                                </router-link>
                            </li>
                            <li class="ml-5">
                                <router-link to="/image-manager/categories">
                                    <i class="mdi mdi-folder-multiple-image"></i> 
                                    <span>{{ t('Categories') }}</span>
                                </router-link>
                            </li>
                        </ul>
                    </li>
                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect">
                            <i class="mdi mdi-settings"></i> <span>{{ t('Bot Manager') }}</span>
                            <span class="menu-arrow float-right"><i class="mdi mdi-chevron-right"></i></span>
                        </a>
                        <ul class="list-unstyled">
                            <li class="ml-5">
                                <router-link to="/bot-manager/create-bot">
                                    <i class="mdi mdi-image"></i> 
                                    <span>{{ t('Create Bot') }}</span>
                                </router-link>
                            </li>
                            <li class="ml-5">
                                <router-link to="/bot-manager/bot-botpress">
                                    <i class="mdi mdi-folder-multiple-image"></i> 
                                    <span>{{ t('Bot Botpress') }}</span>
                                </router-link>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
</template>

<script lang="ts">
import { useI18n } from 'vue-i18n';
import { defineComponent } from 'vue'; // Import defineComponent cho cấu trúc rõ ràng

export default defineComponent({
    name: 'SideMenu', // Đặt tên component rõ ràng
    setup() {
        const { t } = useI18n(); // Khởi tạo hàm dịch ngôn ngữ
        return { t }; // Trả về hàm t để dùng trong template
    },
});
</script>

<style scoped>
/* Đảm bảo các link không active có style mặc định hoặc mong muốn */
.left.side-menu #sidebar-menu ul li a {
    display: block; /* Đảm bảo cả khu vực link là có thể click */
    padding: 12px 20px;
    color: #333; /* Màu chữ mặc định */
    text-decoration: none; /* Bỏ gạch chân */
    transition: background-color 0.3s ease, color 0.3s ease; /* Hiệu ứng chuyển động mượt mà */
}

/* Style cho trạng thái hover */
.left.side-menu #sidebar-menu ul li a:hover {
    background-color: #f0f0f0;
    color: #556ee6; /* Ví dụ: thay đổi màu chữ khi hover */
}

.left.side-menu #sidebar-menu ul li .router-link-active {
    color: #556ee6; /* Màu chữ cho link đang active */
    font-weight: bold; /* Chữ đậm hơn */
    background-color: #c8caca; /* Màu nền cho link đang active */
}
/* Style cho tiêu đề menu */
.left.side-menu #sidebar-menu ul .menu-title {
    padding: 15px 20px 5px;
    color: #999;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: 1px;
}

/* Style cho các icon */
.left.side-menu #sidebar-menu ul li i {
    margin-right: 10px;
    font-size: 18px;
    vertical-align: middle;
}
</style>
