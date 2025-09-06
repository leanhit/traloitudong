import { useI18n } from "vue-i18n";
import { ref, reactive, watch, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { filterDataFunction, splitData, formatDateTime } from "@/until/search";
import { categoryApi } from "@/api/categoryApi";
import { useCategoryStore } from "@/stores/categoryStore"; // Đã đổi sang useCategoryStore
import { useSearchStore } from "@/stores/search";

export default {
    props: ["viewSettings"],
    emits: ["onChangeView"],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const categoryStore = useCategoryStore(); // Sử dụng categoryStore
        const searchStore = useSearchStore();

        const filterData = ref("");
        const filter = ref("ALL");
        const categoriesList = ref([]); // Đã đổi tên biến
        const isLoading = ref(false);
        const tempCategoriesList = ref([]); // Đã đổi tên biến

        const categoryDetail = reactive({ // Đã đổi tên biến
            id: "",
            name: "",
            description: "",
            create_at: "",
        });

        const pagePagination = reactive({
            pageSize: 15,
            currentPage: 1,
            totalItems: 0,
        });

        refreshDataFn();

        async function refreshDataFn() {
            tempCategoriesList.value = [];
            categoriesList.value = [];
            isLoading.value = true;
            try {
                // Gọi API lấy toàn bộ categories
                await categoryStore.getAllCategories();
                tempCategoriesList.value = categoryStore.categories; // Lấy dữ liệu từ store
                pagePagination.totalItems = tempCategoriesList.value.length; // Cập nhật tổng số item
                categoriesList.value = tempCategoriesList.value;
            } catch (err) {
                ElMessage.error(t("Failed to load categories"));
                console.error(err);
            } finally {
                isLoading.value = false;
            }
        }

        const deleteCategory = (id: any) => {
            ElMessageBox.confirm(t("Are you sure you want to delete this category?"), t("Warning"), { // Đổi câu thông báo
                confirmButtonText: t("Yes"),
                cancelButtonText: t("No"),
                type: "warning",
            })
                .then(async () => {
                    isLoading.value = true;
                    try {
                        await categoryApi.deleteCategory(id);
                        ElMessage.success(t("Category deleted successfully")); // Đổi câu thông báo
                        await refreshDataFn();
                        // Dòng này không cần thiết vì refreshDataFn đã làm nhiệm vụ đó
                        // categoriesList.value = splitData(tempCategoriesList.value, pagePagination);
                    } catch (error) {
                        ElMessage.error(t("Failed to delete category")); // Đổi câu thông báo
                    } finally {
                        isLoading.value = false;
                    }
                })
                .catch(() => {
                    ElMessage.info(t("Delete action cancelled"));
                });
        };

        watch(
            () => searchStore.query,
            (newVal) => {
                console.log("Search query changed:", newVal);
                if (!newVal) {
                    categoriesList.value = tempCategoriesList.value;
                } else {
                    categoriesList.value = filterDataFunction(newVal, tempCategoriesList.value);
                }
            }
        );

        const handleSizeChange = (size: number) => {
            pagePagination.pageSize = size;
            categoriesList.value = splitData(tempCategoriesList.value, pagePagination);
        };

        const handleCurrentChange = (page: number) => {
            pagePagination.currentPage = page;
            categoriesList.value = splitData(tempCategoriesList.value, pagePagination);
        };

        return {
            t,
            pagePagination,
            handleCurrentChange,
            handleSizeChange,
            isLoading,
            categoriesList,
            filterData,
            refreshDataFn,
            categoryDetail,
            filter,
            deleteCategory,
            formatDateTime,
        };
    },
};