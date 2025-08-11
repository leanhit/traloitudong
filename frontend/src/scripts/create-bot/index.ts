import { defineAsyncComponent, ref, nextTick } from 'vue';
import SkeletonBox from '@/components/SkeletonBox.vue';
import { useI18n } from 'vue-i18n';

export default {
    components: {
        ListData: defineAsyncComponent({
            loader: () => import('@/views/create-bot/components/List.vue'),
            loadingComponent: SkeletonBox,
        }),
        AddBot: defineAsyncComponent({
            loader: () => import('@/views/create-bot/components/AddBot.vue'),
            loadingComponent: SkeletonBox,
        }),
        EditBot: defineAsyncComponent({
            loader: () => import('@/views/create-bot/components/AddBot.vue'),
            loadingComponent: SkeletonBox,
        })
    },
    setup() {
        const { t } = useI18n();
        const isChangeViewLoading = ref(false);
        const currentComponent = ref('ListData');
        const viewSettings = ref({
            viewName: 'ListData',
            title: t('Bot '),
            dataItem: null,
        });
        const changeView = (paramsObject: { viewName: string; data: any }) => {
            isChangeViewLoading.value = true;
            setTimeout(async () => {
                switch (paramsObject.viewName) {
                    case 'ListData':
                        viewSettings.value = {
                            viewName: 'ListData',
                            title: t("Bot"),
                            dataItem: null,
                        };
                        break;
                    case 'AddBot':
                        viewSettings.value = {
                            viewName: 'AddBot',
                            title: t('Add Bot'),
                            dataItem: paramsObject.data,
                        };
                        break;
                    case 'EditBot':
                        viewSettings.value = {
                            viewName: 'EditBot',
                            title: t('Edit Bot'),
                            dataItem: paramsObject.data,
                        };
                        break;
                }
                await nextTick();
                currentComponent.value = paramsObject.viewName;
                isChangeViewLoading.value = false;
            }, 100);
        };
        return {
            isChangeViewLoading,
            currentComponent,
            viewSettings,
            changeView,
        };
    },
};