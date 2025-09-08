import { useI18n } from "vue-i18n";
import { ref, reactive, watch, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { filterDataFunction, splitData, formatDateTime } from "@/until/search";
import { imageApi } from "@/api/imageApi";
import { useImageStore } from "@/stores/imageStore";
import { useCategoryStore } from "@/stores/categoryStore";
import { useSearchStore } from "@/stores/search";

export default {
    props: ["viewSettings"],
    emits: ["onChangeView"],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const imageStore = useImageStore();
        const searchStore = useSearchStore();
        const categoryStore = useCategoryStore();

        const filterData = ref("");
        const filter = ref("ALL");
        const isLoading = ref(false);

        const imageDetail = reactive({
            id: "",
            name: "",
            description: "",
            url: "",
            tags: "",
            category: "",
            create_at: "",
        });

        const refreshDataFn = async () => {
            isLoading.value = true;
            try {
                // Sử dụng hàm đã có trong store để lấy dữ liệu với phân trang
                await imageStore.getAllImages(imageStore.imagePagination.pageNumber, imageStore.imagePagination.pageSize);
            } finally {
                isLoading.value = false;
            }
        };

        // Watch để tự động tải lại dữ liệu khi trang hoặc kích thước trang thay đổi
        watch(
            () => [imageStore.imagePagination.pageNumber, imageStore.imagePagination.pageSize],
            () => {
                refreshDataFn();
            },
            { immediate: true }
        );

        // Gửi API delete và sau đó refresh data của trang hiện tại
        const deleteImage = async (id: any) => {
            ElMessageBox.confirm(t("Are you sure you want to delete this image?"), t("Warning"), {
                confirmButtonText: t("Yes"),
                cancelButtonText: t("No"),
                type: "warning",
            })
                .then(async () => {
                    isLoading.value = true;
                    try {
                        await imageApi.deleteImage(id);
                        ElMessage.success(t("Image deleted successfully"));
                        // Tải lại dữ liệu của trang hiện tại sau khi xóa thành công
                        await refreshDataFn();
                    } catch (error) {
                        ElMessage.error(t("Failed to delete image"));
                    } finally {
                        isLoading.value = false;
                    }
                })
                .catch(() => {
                    ElMessage.info(t("Delete action cancelled"));
                });
        };

        // Sử dụng watch cho search query
        watch(
            () => searchStore.query,
            async (newVal) => {
                // Bạn có thể xử lý search ở frontend nếu dữ liệu không quá lớn
                // hoặc tốt hơn là truyền query lên server để tìm kiếm hiệu quả hơn
                // Logic tìm kiếm cần được triển khai riêng
                console.log("Search query changed:", newVal);
            }
        );

        // Các hàm xử lý sự kiện phân trang
        const handleSizeChange = (size: number) => {
            imageStore.imagePagination.pageSize = size;
        };

        const handleCurrentChange = (page: number) => {
            imageStore.imagePagination.pageNumber = page - 1; // page của element-plus bắt đầu từ 1, backend bắt đầu từ 0
        };

        return {
            t,
            // Trả về store để component có thể truy cập
            imageStore,
            isLoading,
            imageDetail,
            filter,
            deleteImage,
            formatDateTime,
            handleSizeChange,
            handleCurrentChange,
            getCategoryNameById:categoryStore.getCategoryNameById
        };
    },
};