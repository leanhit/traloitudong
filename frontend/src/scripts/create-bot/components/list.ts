import { useI18n } from 'vue-i18n';
import { ref, reactive, watch, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { filterDataFunction, splitData, formatDateTime } from '@/until/search';
import { botApi } from '@/api/botApi';
import { useSearchStore } from '@/stores/search';
import { useBotStore } from '@/stores/botStore';

export default {
    props: ['viewSettings'],
    emits: ['onChangeView'],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const botStore = useBotStore();
        const searchStore = useSearchStore();

        const filterData = ref('');
        const filter = ref('ALL');
        const listItems = ref([]);
        const isLoading = ref(false);
        const tempList = ref([]);

        const botDetail = reactive({
            id: '',
            botName: '',
            botId: '',
            bot_type: '',
            botDescription: '',
            created_at: '',
        });

        const pagePagination = reactive({
            pageSize: 15,
            currentPage: 1,
            totalItems: 0,
        });

        refreshDataFn();

        async function refreshDataFn() {
            tempList.value = [];
            listItems.value = [];

            await botStore.getAllBots({ page: 0, size: 999 });

            tempList.value = botStore.bot.content;

            botStore.bot.totalItems ? pagePagination.totalItems = botStore.bot.totalItems : pagePagination.totalItems = 0   ;

            listItems.value = tempList.value;
        }

        const deleteBot = (id: any) => {
            ElMessageBox.confirm(
                t('Are you sure you want to delete this bot?'),
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
                        await botApi.deleteBot(id);
                        ElMessage.success(t('Bot deleted successfully'));
                        await refreshDataFn();
                        listItems.value = splitData(
                            botStore.bot.content,
                            pagePagination
                        );
                    } catch (error) {
                        ElMessage.error(t('Failed to delete bot'));
                    } finally {
                        isLoading.value = false;
                    }
                })
                .catch(() => {
                    ElMessage.info(t('Delete action cancelled'));
                });
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

        const getToken=()=>{
            const token = botApi.getToken()
            console.log("token ===>", token.data);
            alert(token.data)
        }

        return {
            t,
            pagePagination,
            handleCurrentChange,
            handleSizeChange,
            isLoading,
            listItems,
            filterData,
            refreshDataFn,
            botDetail,
            filter,
            deleteBot,
            formatDateTime,
            getToken
        };
    },
};
