import { defineAsyncComponent, ref, nextTick } from 'vue';
import SkeletonBox from '@/components/SkeletonBox.vue';
import { useI18n } from 'vue-i18n';

export default {
    components: {
        ListData: defineAsyncComponent({
            loader: () => import('@/views/auto-connect/components/List.vue'),
            loadingComponent: SkeletonBox,
        }),
        AddConnection: defineAsyncComponent({
            loader: () => import('@/views/auto-connect/components/AddConnection.vue'),
            loadingComponent: SkeletonBox,
        }),
        EditConnection: defineAsyncComponent({
            loader: () => import('@/views/auto-connect/components/AddConnection.vue'),
            loadingComponent: SkeletonBox,
        })
    },
    setup() {
        const { t } = useI18n();
        const isChangeViewLoading = ref(false);
        const currentComponent = ref('ListData');
        const viewSettings = ref({
            viewName: 'ListData',
            title: t('Configs '),
            dataItem: null,
        });
        const changeView = (paramsObject: { viewName: string; data: any }) => {
            isChangeViewLoading.value = true;
            setTimeout(async () => {
                switch (paramsObject.viewName) {
                    case 'ListData':
                        viewSettings.value = {
                            viewName: 'ListData',
                            title: t("Configs"),
                            dataItem: null,
                        };
                        break;
                    case 'AddConnection':
                        viewSettings.value = {
                            viewName: 'AddConnection',
                            title: t('Add Connection'),
                            dataItem: paramsObject.data,
                        };
                        break;
                    case 'EditConnection':
                        viewSettings.value = {
                            viewName: 'EditConnection',
                            title: t('Edit Connection'),
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