<template>
    <form role="search" class="custom-search-form">
        <input
            v-model="searchQuery"
            @keyup.enter="updateSearch"
            type="text"
            :placeholder="t('search')"
            class="form-control" />
        <button
            @click.prevent="updateSearch"
            type="submit"
            class="search-icon-btn">
            <i class="fa fa-search"></i>
        </button>
    </form>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { useSearchStore } from '@/stores/search';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const route = useRoute();

const searchStore = useSearchStore();
const searchQuery = computed({
    get: () => searchStore.query,
    set: (val) => searchStore.setQuery(val),
});

const updateSearch = () => {
    const currentPath = route.path;

    if (currentPath.startsWith('/create-connection')) {
        searchStore.setQuery(searchQuery.value);
    } else if (currentPath.startsWith('/admin')) {
        // Xử lý tìm kiếm riêng nếu cần
    }
};
</script>

<style scoped>
/* Custom styles */
.custom-search-form {
    position: relative;
    /* ĐẶT CHIỀU RỘNG GẤP ĐÔI TẠI ĐÂY */
    /* Ví dụ: Nếu chiều rộng mặc định là 200px, bạn đặt 400px. */
    /* Cần kiểm tra chiều rộng mặc định của .app-search hoặc .form-control trong dự án của bạn để nhân đôi cho chính xác. */
    width: 360px; /* Đây là ví dụ, giả sử chiều rộng mặc định của bạn là 180px */
}

.custom-search-form .form-control {
    /* Giữ màu nền mặc định hoặc màu bạn muốn */
    /* background-color: #ffffff; /* Ví dụ: màu trắng tinh nếu bạn muốn */
    padding-right: 40px; /* Đảm bảo đủ khoảng trống cho icon */
    width: 100%; /* Đảm bảo input chiếm toàn bộ chiều rộng của form */
}

.search-icon-btn {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    background: transparent;
    border: none;
    padding: 0 10px;
    cursor: pointer;
    color: #555; /* Màu icon mặc định */
    transition: color 0.2s ease;
}

.search-icon-btn:hover {
    color: #333; /* Màu icon khi hover */
}

/* Đảm bảo list-inline-item không giới hạn chiều rộng nếu nó có style ẩn */
.list-inline-item {
    max-width: none;
    width: auto;
}
</style>
