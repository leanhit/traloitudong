import { useI18n } from 'vue-i18n';
import { ref, reactive, watch, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { filterDataFunction, splitData, formatDateTime } from '@/until/search';
import { fbConnectionApi } from '@/api/fbConnectionApi';
import { useDataconnectionStore } from '@/stores/connectionStore';
import { useSearchStore } from '@/stores/search';

export default {
    props: ['viewSettings'],
    emits: ['onChangeView'],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const connectionStore = useDataconnectionStore();
        const searchStore = useSearchStore();

        const filterData = ref('');
        const filter = ref('ALL');
        const listItems = ref([]);
        const isLoading = ref(false);
        const tempList = ref([]);

        const pagePagination = reactive({
            pageSize: 15,
            currentPage: 1,
            totalItems: 0,
        });

        refreshDataFn();

        async function refreshDataFn() {
            tempList.value = [];
            listItems.value = [];

            await connectionStore.getAllConnections({ page: 999, size: 999 });
            tempList.value = connectionStore.connection.content;

            pagePagination.totalItems =
                connectionStore.connection.totalElements;

            listItems.value = tempList.value;
        }

        const deleteConfig = (id: any) => {
            ElMessageBox.confirm(
                t('Are you sure you want to delete this connection?'),
                t('Warning'),
                {
                    confirmButtonText: t('Yes'),
                    cancelButtonText: t('No'),
                    type: 'warning',
                }
            )
                .then(async () => {
                    isLoading.value = true;
                    try {
                        await fbConnectionApi.deleteConfig(id);
                        ElMessage.success(t('Config deleted successfully'));
                        await refreshDataFn();
                        listItems.value = splitData(
                            connectionStore.connection.content,
                            pagePagination
                        );
                    } catch (error) {
                        ElMessage.error(t('Failed to delete connection'));
                    } finally {
                        isLoading.value = false;
                    }
                })
                .catch(() => {
                    ElMessage.info(t('Delete action cancelled'));
                });
        };

        const toggleStatus = async (itemData: any, newStatus: boolean) => {
            try {
                isLoading.value = true;
                const updatedData = { ...itemData, enabled: newStatus };

                const res = await fbConnectionApi.updateConfig(
                    itemData.id,
                    updatedData
                );

                if (res.data) {
                    itemData.enabled = newStatus;
                    ElMessage.success(t('Status updated successfully'));
                } else {
                    ElMessage.error(t('Failed to update status'));
                }
            } catch (err) {
                console.error(err);
                ElMessage.error(t('Error updating status'));
            } finally {
                isLoading.value = false;
            }
        };

        watch(
            () => searchStore.query,
            (newVal) => {
                console.log('Search query changed:', newVal);
                if (!newVal) {
                    listItems.value = tempList.value; // Nếu xóa ô search thì show lại full list
                } else {
                    listItems.value = filterDataFunction(
                        newVal,
                        tempList.value
                    );
                }
            }
        );

        //watch(filterData, () => (listItems.value = filterDataFunction(filterData.value, tempList.value)));

        const handleSizeChange = (size: number) => {
            pagePagination.pageSize = size;
            listItems.value = splitData(tempList.value, pagePagination);
        };

        const handleCurrentChange = (page: number) => {
            pagePagination.currentPage = page;
            listItems.value = splitData(tempList.value, pagePagination);
        };

        return {
            t,
            pagePagination,
            handleCurrentChange,
            handleSizeChange,
            isLoading,
            listItems,
            filterData,
            refreshDataFn,
            filter,
            deleteConfig,
            formatDateTime,
            toggleStatus,
        };
    },
};
