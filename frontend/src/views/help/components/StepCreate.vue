<script lang="ts" src="@/scripts/help/components/stepCreate.ts"></script>
<template>
    <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
        <!-- header -->
        <div class="d-flex align-items-center justify-content-between pb-3">
            <div class="page-titles">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item">
                        <a href="javascript:void(0)">{{ t('Help') }}</a>
                    </li>
                    <li class="breadcrumb-item active">
                        <a href="javascript:void(0)">{{
                            viewSettings.title
                        }}</a>
                    </li>
                </ol>
            </div>
            <div class="d-flex align-items-center">
                <div class="ml-1 mr-4 w-100">
                    <el-button
                        size="default"
                        type="danger"
                        class="d-none d-md-block"
                        @click="
                            $emit('onChangeView', {
                                viewName: 'ListData',
                                data: null,
                            })
                        ">
                        <div>Back</div>
                    </el-button>
                </div>
            </div>
        </div>

        <!-- body -->
        <div class="card mt-2">
            <div class="card-body">
                <h5 class="mb-4">{{ t('Create Connection Steps') }}</h5>

                <ol>
                    <li>
                        <h6 class="font-weight-bold">
                            <a
                                href="https://admin.traloitudong.com/home"
                                target="_blank">
                                1: Tạo cấu hình kết nối mới trên hệ thống admin
                                botpress
                            </a>
                        </h6>
                        <p class="text-muted mb-2">
                            Lưu ý 2 trường urlCallback và verifyToken sẽ được
                            dùng để kết nối fb với botpress
                        </p>
                    </li>
                    <li>
                        <h6 class="font-weight-bold">
                            <a
                                href="https://bot.traloitudong.com/admin/workspace/default/bots"
                                target="_blank">
                                2: Tạo app botpress trên botpress
                            </a>
                        </h6>
                        <p class="text-muted mb-2">
                            Tên app này nên giống với tên app trên fb ở bước
                            3.1.1, lưu lại thông tin, bot_id và botName vào
                            config
                        </p>
                    </li>
                    <li>
                        <h6 class="font-weight-bold">
                            <a
                                href="https://www.facebook.com/pages/creation"
                                target="_blank">
                                3: Tạo Fanpage Facebook
                            </a>
                        </h6>
                        <p class="text-muted mb-2">
                            Truy cập Facebook, tạo fanpage mới hoặc sử dụng
                            fanpage sẵn có. Lưu url fanpage lại config ở step 1
                        </p>
                    </li>
                    <li>
                        <h6 class="font-weight-bold">
                            <a
                                href="https://developers.facebook.com"
                                target="_blank">
                                4: Tạo App Facebook
                            </a>
                        </h6>
                        <p class="text-muted mb-2">4.1: Tạo app fb</p>
                        <ul>
                            <li>
                                <a>
                                    4.1.1 : Khi đặt tên app trên fb nên đặt tên
                                    giống app trên botpress và tên trang fanpage
                                    cho dễ quản lý.
                                </a>
                            </li>
                            <li>
                                <a>
                                    4.1.2 : Ở bước "Thêm trường hợp sử dụng"
                                    chọn "Khác - Ứng dụng của bạn sẽ được tạo
                                    trong trải nghiệm cũ. Sau đó, bạn sẽ chọn từ
                                    tất cả các quyền, tính năng và sản phẩm có
                                    thể sử dụng." ở dưới cùng.</a
                                >
                            </li>
                            <li>
                                <a>
                                    4.1.3 : Ở bước " "Chọn loại ứng dụng" chọn
                                    "Kinh doanh".</a
                                >
                            </li>
                            <li>
                                <a>
                                    4.1.4 : Ở bước " "Thêm sản phẩm vào ứng
                                    dụng" chọn "Messenger" ấn nút "Thiết
                                    lập".</a
                                >
                            </li>
                        </ul>
                        <p class="text-muted mb-2">4.2: Cấu hình app fb</p>
                        <ul>
                            <li>
                                <a>
                                    4.2.1 : Ở mục "Messenger" (Danh sách menu
                                    bên trái), tại bước "2. Tạo mã truy cập" kết
                                    nối với trang fanpage ở bước 2, lưu lại
                                    pageId, pageAccessToken vào cấu hình ở step
                                    1.</a
                                >
                            </li>
                            <li>
                                <a>
                                    4.2.2 : Ở mục "Cài đặt ứng dụng/Thông tin cơ
                                    bản", lấy "app_scret - Khóa bí mật của ứng
                                    dụng", lưu vào config ở bước 1</a
                                >
                            </li>
                            <li>
                                <a>
                                    4.2.3 : Ở mục "Cài đặt ứng dụng/Thông tin cơ
                                    bản", điền URL chính sách quyền riêng tư,
                                    dùng cái có sẵn
                                    "ttps://www.privacypolicies.com/live/99ee657d-59fe-483a-8d5b-4a732dcf77b1"</a
                                ><br /><a>Nhớ lưu thay đổi</a>
                            </li>
                            <li>
                                <a>
                                    4.2.4 : Tại trang Config, khi các trường
                                    được điền đầy đủ, ấn nút "Xác minh" để tạo
                                    kết nối với fb.</a
                                >
                            </li>
                            <li>
                                <a>
                                    4.2.1 : Quay lại "Messenger" (Danh sách menu
                                    bên trái), điền URL callback và verify token
                                    đã tạo ở tràn config vào các trường "URL máy
                                    chủ" và "Mã thông báo xác minh" tương ứng.
                                    Sau đó, ấn nút "Xác minh và lưu" để lưu cấu
                                    hình.</a
                                >
                            </li>
                            <li>
                                <a>
                                    4.2.1 : Tại "Chế độ của ứng dụng: " bật "Chế
                                    độ phát triển" để có thể gửi tin nhắn từ bot
                                    đến người dùng.</a
                                >
                            </li>
                            <li>
                                <a>
                                    4.2.1 : Tại mục "Messenger" (Danh sách menu
                                    bên trái), tại bước "2. Tạo mã truy cập /Gói
                                    đăng ký Webhoo" Bấm "thêm đăng ký", chọn hết
                                    các checkbox rồi ấn Confirm</a
                                >
                            </li>
                        </ul>
                    </li>
                    <li>Done</li>
                </ol>
            </div>
        </div>
    </div>
</template>

<style scoped>
.card-body {
    font-size: 14px;
}
</style>
