// src/views/auto-connect/list.ts

import { useI18n } from 'vue-i18n';
import { ref, reactive, watch, onMounted } from 'vue';
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus';
import { filterDataFunction, splitData, formatDateTime } from '@/until/search';
import { fbConnectionApi } from '@/api/fbConnectionApi';
import { useDataconnectionStore } from '@/stores/connectionStore';
import { useSearchStore } from '@/stores/search';
import { useFacebookStore } from '@/stores/facebook';
import { sendAddConnections } from './autoConnectHandler'; // Đã tách

export default {
    props: ['viewSettings'],
    emits: ['onChangeView'],
    setup(props, context) {
        const { t } = useI18n();
        const connectionStore = useDataconnectionStore();
        const searchStore = useSearchStore();
        const facebookStore = useFacebookStore();

        const filterData = ref('');
        const filter = ref('ALL');
        const listItems = ref([]);
        const isLoading = ref(false);
        const tempList = ref([]);
        const connectedPageIds = ref([]); // State để lưu pageId đã kết nối

        const pagePagination = reactive({
            pageSize: 15,
            currentPage: 1,
            totalItems: 0,
        });

        const selectedBotId = ref('traloitudong');
        const botIdOptions = ref([
            { name: "Bot test", value: "traloitudong" },
            { name: "khoa học", value: "testflowqa" },
            { name: "Loathongbao", value: "tingbox" },
        ]);

        const refreshDataFn = async () => {
            isLoading.value = true;
            try {
                await connectionStore.getAllConnections({ page: 999, size: 999 });
                tempList.value = connectionStore.connection.content;
                pagePagination.totalItems = connectionStore.connection.totalElements;
                listItems.value = splitData(
                    tempList.value,
                    pagePagination
                );
                console.log('Fetched connections:', tempList.value);
            } catch (error) {
                console.error('Failed to fetch connections:', error);
                ElMessage.error('Lỗi khi tải danh sách kết nối.'); // Thêm thông báo lỗi
            } finally {
                isLoading.value = false;
            }
        };

        const fetchConnectedPageIds = async () => {
            try {
                const response = await fbConnectionApi.getAllConnections({ page: 999, size: 999 });
                if (response.data && Array.isArray(response.data.content)) {
                    connectedPageIds.value = response.data.content.map(conn => conn.pageId);
                }
            } catch (error) {
                console.error("Lỗi khi lấy danh sách kết nối:", error);
            }
        };

        // Hàm duy nhất để xử lý toàn bộ quá trình auto-connect
        const handleAutoConnect = () => {
            if (typeof window.FB === "undefined") {
                console.error("❌ Facebook SDK chưa load!");
                ElMessage.error("Facebook SDK chưa load! Vui lòng kiểm tra lại.");
                return;
            }

            const botpressPermissions = [
                "public_profile",
                "email",
                "pages_messaging",
                "pages_show_list",
                "pages_read_engagement",
                "pages_manage_posts"
            ];

            window.FB.login(
                (response) => {
                    if (response.authResponse) {
                        const { accessToken, userID } = response.authResponse;
                        facebookStore.setFacebookData({ accessToken, userID });
                        // Gọi hàm đã tách
                        sendAddConnections(accessToken, selectedBotId.value, refreshDataFn);
                    } else {
                        console.error('Login error: Đăng nhập bị hủy hoặc không cấp quyền.');
                        ElMessage.error('Đăng nhập Facebook thất bại.');
                    }
                }, {
                scope: botpressPermissions.join(",")
            }
            );
        };
        
        onMounted(() => {
            refreshDataFn();
            fetchConnectedPageIds();
        });

        const deleteConfig = async (id) => {
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

        const toggleStatus = async (itemData, newStatus) => {
            try {
                isLoading.value = true;
                const updatedData = { ...itemData, isEnabled: newStatus };

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
                if (!newVal) {
                    listItems.value = splitData(tempList.value, pagePagination);
                } else {
                    const filteredData = filterDataFunction(
                        newVal,
                        tempList.value
                    );
                    listItems.value = splitData(filteredData, pagePagination);
                    pagePagination.totalItems = filteredData.length;
                }
            }
        );

        const handleSizeChange = (size) => {
            pagePagination.pageSize = size;
            listItems.value = splitData(tempList.value, pagePagination);
        };

        const handleCurrentChange = (page) => {
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
            showFacebookLoginModal: handleAutoConnect, // Đổi tên hàm
            connectedPageIds,
            botIdOptions,
            selectedBotId,
        };
    },
};