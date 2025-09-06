import { ref, onMounted, reactive, watch } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useI18n } from 'vue-i18n';
import { categoryApi } from '@/api/categoryApi';
import { useCategoryStore } from '@/stores/categoryStore';

export default {
    props: ['viewSettings'],
    emits: ['onChangeView'],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const categoryStore = useCategoryStore();
        const isLoading = ref(false);
        const viewName = ref("");
        const formRef = ref<FormInstance>();

        // Model cho dữ liệu danh mục
        const itemModel = ref({
            id: '',
            name: '',
            description: '',
        });

        // Form validation rules
        const rules: FormRules = {
            name: [{ required: true, message: t('Tên danh mục là bắt buộc'), trigger: 'blur' }],
            description: [{ required: true, message: t('Mô tả là bắt buộc'), trigger: 'blur' }],
        };

        onMounted(() => {
            viewName.value = props.viewSettings.viewName;
            console.log("viewName", viewName.value);

            if (viewName.value === 'AddCategory') {
                // Reset model khi thêm mới
                itemModel.value = {
                    id: '',
                    name: '',
                    description: '',
                };
            } else if (viewName.value === 'EditCategory') {
                // Gán dữ liệu danh mục hiện có để chỉnh sửa
                const dataItem = props.viewSettings.data;
                itemModel.value = {
                    id: dataItem.id || '',
                    name: dataItem.name || '',
                    description: dataItem.description || '',
                };
            } else {
                console.log("Something went wrong with viewName:", viewName.value);
            }
        });

        // Xử lý gửi form
        async function onSubmit(formEl: FormInstance | undefined) {
            isLoading.value = true;
            if (!formEl) {
                isLoading.value = false;
                return;
            }

            formEl.validate(async (valid) => {
                if (valid) {
                    const dataToSend = {
                        name: itemModel.value.name,
                        description: itemModel.value.description,
                    };

                    try {
                        let response: any;
                        if (viewName.value === 'AddCategory') {
                            response = await categoryApi.addCategory(dataToSend);
                        } else if (viewName.value === 'EditCategory') {
                            response = await categoryApi.updateCategory(itemModel.value.id, dataToSend);
                        } else {
                            ElMessage.error(t("Chế độ không hợp lệ!"));
                            isLoading.value = false;
                            return;
                        }

                        if (response.data) {
                            ElMessage({
                                message: t('Thành công!'),
                                type: 'success',
                            });
                            context.emit('onChangeView', {
                                viewName: 'ListData', // Trở về danh sách sau khi thành công
                                data: null,
                            });
                        } else {
                            ElMessage.error(`Oops, ${response.message}`);
                        }
                    } catch (error: any) {
                        console.error(error);
                        ElMessage.error(t(`Đã xảy ra lỗi: ${error.message || 'Không xác định'}`));
                    } finally {
                        isLoading.value = false;
                    }
                } else {
                    console.log('error submit!');
                    ElMessage.error(t('Vui lòng kiểm tra lại các trường bị lỗi.'));
                    isLoading.value = false;
                }
            });
        }

        return {
            t,
            isLoading,
            itemModel,
            formRef,
            rules,
            viewName,
            onSubmit,
        };
    },
};