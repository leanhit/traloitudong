import { defineAsyncComponent, ref, nextTick } from 'vue';
import SkeletonBox from '@/components/SkeletonBox.vue';
import { useI18n } from 'vue-i18n';

export default {
    components: {
        ListData: defineAsyncComponent({
            loader: () => import('@/views/image-manager/components/List.vue'),
            loadingComponent: SkeletonBox,
        }),
        AddImage: defineAsyncComponent({
            loader: () => import('@/views/image-manager/components/AddImage.vue'),
            loadingComponent: SkeletonBox,
        }),
        EditImage: defineAsyncComponent({
            loader: () => import('@/views/image-manager/components/AddImage.vue'),
            loadingComponent: SkeletonBox,
        })
    },
    setup() {
        const { t } = useI18n();
        const isChangeViewLoading = ref(false);
        const currentComponent = ref('ListData');
        const viewSettings = ref({
            viewName: 'ListData',
            title: t('Image Manager'),
            dataItem: null,
        });
        const changeView = (paramsObject: { viewName: string; data: any }) => {
            isChangeViewLoading.value = true;
            setTimeout(async () => {
                switch (paramsObject.viewName) {
                    case 'ListData':
                        viewSettings.value = {
                            viewName: 'ListData',
                            title: t("Image Manager"),
                            dataItem: null,
                        };
                        break;
                    case 'AddImage':
                        viewSettings.value = {
                            viewName: 'AddImage',
                            title: t('Add Image'),
                            dataItem: paramsObject.data,
                        };
                        break;
                    case 'EditImage':
                        viewSettings.value = {
                            viewName: 'EditImage',
                            title: t('Edit Image'),
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