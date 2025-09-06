import { defineAsyncComponent, ref, nextTick } from 'vue';
import SkeletonBox from '@/components/SkeletonBox.vue';
import { useI18n } from 'vue-i18n';

export default {
    components: {
        ListData: defineAsyncComponent({
            loader: () => import('@/views/image-manager/category/components/List.vue'),
            loadingComponent: SkeletonBox,
        }),
        AddCategory: defineAsyncComponent({
            loader: () => import('@/views/image-manager/category/components/AddCategory.vue'),
            loadingComponent: SkeletonBox,
        }),
        EditCategory: defineAsyncComponent({
            loader: () => import('@/views/image-manager/category/components/AddCategory.vue'),
            loadingComponent: SkeletonBox,
        })
    },
    setup() {
        const { t } = useI18n();
        const isChangeViewLoading = ref(false);
        const currentComponent = ref('ListData');
        const viewSettings = ref({
            viewName: 'ListData',
            title: t('Category'),
            dataItem: null,
        });
        const changeView = (paramsObject: { viewName: string; data: any }) => {
            isChangeViewLoading.value = true;
            setTimeout(async () => {
                switch (paramsObject.viewName) {
                    case 'ListData':
                        viewSettings.value = {
                            viewName: 'ListData',
                            title: t("Category"),
                            dataItem: null,
                        };
                        break;
                    case 'AddCategory':
                        viewSettings.value = {
                            viewName: 'AddCategory',
                            title: t('Add Category'),
                            dataItem: paramsObject.data,
                        };
                        break;
                    case 'EditCategory':
                        viewSettings.value = {
                            viewName: 'EditCategory',
                            title: t('Edit Category'),
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