import { ref } from 'vue'
import { useI18n } from "vue-i18n";

export default {
    props: ["viewSettings"],
    emits: ["onChangeView"],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const isLoading = ref(false)
        const stepList = ref([
            {
                title: 'Tạo Bot trên Botpress',
                description: 'Đăng nhập vào hệ thống Botpress, tạo mới một bot và lưu lại Bot ID.'
            },
            {
                title: 'Tạo Fanpage Facebook',
                description: 'Truy cập Facebook, tạo fanpage mới hoặc sử dụng fanpage sẵn có.'
            },
            {
                title: 'Lấy Page Access Token',
                description: 'Vào Meta for Developers, tạo App, cấp quyền cho fanpage và lấy access token.'
            },
            {
                title: 'Cấu hình Callback URL và Verify Token',
                description: 'Nhập các giá trị Callback URL và Verify Token trên Meta Developer App.'
            },
            {
                title: 'Thêm thông tin cấu hình vào hệ thống',
                description: 'Quay lại hệ thống quản lý, nhấn "Add Connection" và nhập các thông tin tương ứng.'
            }
        ])

        return {
            t,
            isLoading,
            stepList
        };
    },
};
