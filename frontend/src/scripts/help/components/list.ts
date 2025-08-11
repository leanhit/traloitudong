import { ref } from 'vue'
import { useI18n } from "vue-i18n";

export default {
    props: ["viewSettings"],
    emits: ["onChangeView"],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const isLoading = ref(false)

        const helpTopics = ref([
            {
                title: 'Cách thêm cấu hình Bot',
                content: 'Nhấn vào nút "Add Connection" ở góc trên bên phải, điền đầy đủ các thông tin và nhấn "Lưu".'
            },
            {
                title: 'Chỉnh sửa cấu hình đã có',
                content: 'Chọn biểu tượng ba chấm (...) tại mỗi dòng cấu hình, sau đó chọn "Edit Connection".'
            },
            {
                title: 'Xóa cấu hình',
                content: 'Tương tự chỉnh sửa, chọn "Delete Config" từ menu hành động.'
            },
            {
                title: 'Tải lại danh sách',
                content: 'Nhấn nút "Check" để làm mới danh sách cấu hình từ server.'
            }
        ])
        return {
            t,
            isLoading,
            helpTopics
        };
    },
};
