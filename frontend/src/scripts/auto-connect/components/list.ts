import { useI18n } from 'vue-i18n';
import { ref, reactive, watch, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { filterDataFunction, splitData, formatDateTime } from '@/until/search';
import { fbConnectionApi } from '@/api/fbConnectionApi';
import { useDataconnectionStore } from '@/stores/connectionStore';
import { useSearchStore } from '@/stores/search';
import { useFacebookStore } from '@/stores/facebook';
import FanpageSelectionModal from '@/views/auto-connect/components/FanpageSelectionModal.vue';

export default {
    components: { FanpageSelectionModal },
    props: ['viewSettings'],
    emits: ['onChangeView'],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const connectionStore = useDataconnectionStore();
        const searchStore = useSearchStore();
        const facebookStore = useFacebookStore();

        const filterData = ref('');
        const filter = ref('ALL');
        const listItems = ref([]);
        const isLoading = ref(false);
        const tempList = ref([]);
        const isShowModal = ref(false);
        const pages = ref([]);

        const pagePagination = reactive({
            pageSize: 15,
            currentPage: 1,
            totalItems: 0,
        });

        // Hàm xử lý đăng nhập thành công và lấy danh sách fanpage
        const handleLoginSuccess = async (data: any) => {
            facebookStore.setFacebookData({
                accessToken: data.accessToken,
                userID: data.userID,
            });

            try {
                const response = await fetch(
                    `https://graph.facebook.com/v18.0/me/accounts?fields=id,name,access_token,picture.width(100).height(100)&access_token=${data.accessToken}`
                );
                const pagesData = await response.json();

                if (pagesData?.data) {
                    pages.value = pagesData.data.map((page: any) => ({
                        pageId: page.id,
                        pageName: page.name,
                        pageAccessToken: page.access_token,
                        thumbnail: page.picture?.data?.url,
                    }));
                    
                    isShowModal.value = true;
                } else {
                    console.error('No page data found.');
                    ElMessage.error('No fanpages found. Please check permissions.');
                }
            } catch (error) {
                console.error('Error fetching page list:', error);
                ElMessage.error('Error fetching fanpage list from Facebook.');
            }
        };

        const handleLoginFailure = (error: any) => {
            console.error('Login error:', error);
            ElMessage.error('Facebook login failed.');
        };

        // Hàm được gọi khi nhấn nút "Add Connection"
        const showFacebookLoginModal = () => {
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
                        handleLoginSuccess(response.authResponse);
                    } else {
                        handleLoginFailure("Đăng nhập bị hủy hoặc không cấp quyền.");
                    }
                }, {
                    scope: botpressPermissions.join(",")
                }
            );
        };

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
            } finally {
                isLoading.value = false;
            }
        };

        onMounted(refreshDataFn);

        const deleteConfig = async (id: any) => {
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

        const toggleStatus = async (itemData: any, newStatus: boolean) => {
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

        const actionAddConnections = async (pages: any[]) => {
            if (!Array.isArray(pages) || pages.length === 0) {
                ElMessage.error('Invalid data. Please select at least one page.');
                return;
            }

            isLoading.value = true;

            const cleanedPages = pages.map(page => {
                const cleanPage = { ...page };
                delete cleanPage.id;
                delete cleanPage.createdAt;
                delete cleanPage.lastUpdatedAt;
                return cleanPage;
            });

            try {
                const response = await fbConnectionApi.addConnections(cleanedPages);
                if (response.data) {
                    ElMessage({
                        message: t('Connections added successfully!'),
                        type: 'success',
                    });
                    await refreshDataFn();
                } else {
                    ElMessage.error(`Oops, ${response.message}`);
                }
            } catch (error) {
                console.error(error);
                ElMessage.error(t('An error occurred.'));
            } finally {
                isLoading.value = false;
                isShowModal.value = false;
            }
        };

        const handleConnectPage = (page: any) => {
            actionAddConnections([page]);
        };

        const handleConnectAllPages = (pagesToConnect: any[]) => {
            actionAddConnections(pagesToConnect);
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
            isShowModal,
            showFacebookLoginModal,
            handleLoginSuccess,
            handleLoginFailure,
            handleConnectPage,
            handleConnectAllPages,
            pages,
        };
    },
};