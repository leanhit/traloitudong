// src/views/auto-connect/components/FanpageSelectionModal.vue

import { ref, defineComponent, onMounted } from 'vue';
import { ElMessage, ElSelect, ElOption } from 'element-plus';
import { fbConnectionApi } from '@/api/fbConnectionApi';

export default defineComponent({
    components: {
        ElSelect,
        ElOption,
    },
    props: {
        pages: {
            type: Array,
            required: true,
            default: () => [],
        },
        connectedPageIds: {
            type: Array,
            required: true,
            default: () => [],
        },
        bot: {
            type: String,
            required: true,
            default: () => { }
        }
    },
    emits: ['connectSuccess', 'close'],
    setup(props, { emit }) {
        const isLoading = ref(false);
        const selectedBotId = ref('');

        const botIdOptions = ref([]);

        console.log("=====>", props.bot)
        const addConnections = async (pagesToConnect) => {
            if (!Array.isArray(pagesToConnect) || pagesToConnect.length === 0) {
                ElMessage.error('Dữ liệu không hợp lệ. Vui lòng chọn ít nhất một trang.');
                return false;
            }
            if (!selectedBotId.value) {
                ElMessage.warning('Vui lòng chọn một Bot ID trước khi kết nối.');
                return false;
            }

            const cleanedPages = pagesToConnect.map(page => ({
                botId: selectedBotId.value,
                botName: page.pageName,
                pageId: page.pageId,
                fanpageUrl: `https://www.facebook.com/${page.pageId}`,
                pageAccessToken: page.pageAccessToken,
                isEnabled: true,
            }));

            try {
                const response = await fbConnectionApi.addConnectionsClient(cleanedPages);
                if (response.data) {
                    ElMessage({
                        message: 'Connections added successfully!',
                        type: 'success',
                    });
                    return true;
                } else {
                    ElMessage.error(`Oops, ${response.message}`);
                    return false;
                }
            } catch (error) {
                console.error(error);
                ElMessage.error('Đã xảy ra lỗi. Vui lòng thử lại sau.');
                return false;
            }
        };

        const connectPage = async (page) => {
            isLoading.value = true;
            const isSuccess = await addConnections([page]);
            isLoading.value = false;
            if (isSuccess) {
                emit('connectSuccess');
                emit('close');
            }
        };

        const connectAllPages = async () => {
            isLoading.value = true;
            const pagesToConnect = props.pages.filter(page => !isPageConnected(page.pageId));
            const isSuccess = await addConnections(pagesToConnect);
            isLoading.value = false;
            if (isSuccess) {
                emit('connectSuccess');
                emit('close');
            }
        };

        const isPageConnected = (pageId) => {
            return props.connectedPageIds.includes(pageId);
        };

        const truncateToken = (token) => {
            return `${token.substring(0, 8)}...${token.substring(token.length - 8)}`;
        };

        const copyToken = (token) => {
            navigator.clipboard.writeText(token).then(() => {
                ElMessage.success('Đã sao chép token!');
            }).catch(err => {
                console.error('Lỗi khi sao chép:', err);
                ElMessage.error('Không thể sao chép token.');
            });
        };

        const getPageUrl = (pageId) => {
            return `https://www.facebook.com/${pageId}`;
        };

        onMounted(() => {
            selectedBotId.value = props.bot.botId;
            botIdOptions.value = props.bot;

        });


        return {
            isLoading,
            selectedBotId,
            botIdOptions,
            connectPage,
            connectAllPages,
            truncateToken,
            copyToken,
            getPageUrl,
            isPageConnected,
        };
    },
});