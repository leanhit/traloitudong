import { ref, reactive, onMounted } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage } from 'element-plus';
import { fbConnectionApi } from '@/api/fbConnectionApi';
import { useI18n } from 'vue-i18n';

export default {
    props: ['viewSettings'],
    emits: ['onChangeView'],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const isLoading = ref(false);
        const viewName = ref('');
        const formRef = ref<FormInstance>();
        const copiedKey = ref(null);

        // Các trường chung cho cả hai chế độ Thêm và Sửa
        const commonFields = {
            botName: 'Connection Name (Bot Name)',
            botId: 'Bot ID',
        };

        // Các trường chỉ hiển thị khi ở chế độ Sửa
        const editOnlyFields = {
            //isEnabled: 'Enabled',
            createdAt: 'Created At',
            lastUpdatedAt: 'Last Updated At',
        };

        const itemModel = ref({
            id: '',
            botId: '',
            botName: '',
            pageId: '',
            fanpageUrl: '',
            pageAccessToken: '',
            isEnabled: true,
            createdAt: '',
            lastUpdatedAt: '',
        });

        // Form validation rules
        const rules = reactive<FormRules>({
            botId: [
                {
                    required: true,
                    message: 'Bot ID là bắt buộc',
                    trigger: 'blur',
                },
            ],
            pageId: [
                {
                    required: true,
                    message: 'Page ID là bắt buộc',
                    trigger: 'blur',
                },
            ],
            appSecret: [
                {
                    required: true,
                    message: 'App Secret là bắt buộc',
                    trigger: 'blur',
                },
            ],
            pageAccessToken: [
                {
                    required: true,
                    message: 'Page Access Token là bắt buộc',
                    trigger: 'blur',
                },
            ],
            verifyToken: [
                {
                    required: true,
                    message: 'Verify Token là bắt buộc',
                    trigger: 'blur',
                },
            ],
        });

        onMounted(() => {
            viewName.value = props.viewSettings.viewName;

            if (viewName.value === 'AddConnection') {
                // Đặt giá trị mặc định cho chế độ thêm
                itemModel.value.id = '';
                itemModel.value.createdAt = '';
                itemModel.value.lastUpdatedAt = '';
                // Giữ nguyên các giá trị mặc định khác như isEnabled: true
            } else if (
                viewName.value === 'EditConnection' ||
                viewName.value === 'CloneConfig'
            ) {
                // Ánh xạ dữ liệu từ props khi ở chế độ chỉnh sửa hoặc sao chép
                const dataFromApi = props.viewSettings.dataItem;

                // Tạo một đối tượng mới để gán, ánh xạ thủ công 'enabled' sang 'isEnabled'
                const mappedData = {
                    ...dataFromApi,
                    isEnabled: dataFromApi.enabled
                };

                Object.assign(itemModel.value, mappedData);

                // Xóa ID và ngày tháng nếu ở chế độ 'Clone'
                if (viewName.value === 'CloneConfig') {
                    itemModel.value.id = '';
                    itemModel.value.createdAt = '';
                    itemModel.value.lastUpdatedAt = '';
                }

            } else {
                console.log('Something went wrong')!;
            }
        });

        function onSubmit(formEl: FormInstance | undefined) {
            isLoading.value = true;

            if (!formEl) return;
            formEl.validate((valid) => {
                if (valid) {
                    const data = { ...itemModel.value };

                    if (viewName.value === 'AddConnection') {
                        actionAddData(data);
                    } else if (viewName.value === 'EditConnection') {
                        // Tạo một đối tượng mới để gán, ánh xạ thủ công 'enabled' sang 'isEnabled'
                        const mappedData = {
                            ...data,
                            isEnabled: data.enabled
                        };
                        actionEditData(data);
                    } else {
                        console.log(viewName.value);
                    }
                } else {
                    console.log('error submit!');
                    isLoading.value = false;
                }
            });
        }

        function actionAddData(data: any) {
            // Xóa các trường không cần thiết khi thêm mới
            delete data.id;
            delete data.createdAt;
            delete data.lastUpdatedAt;

            const datas = [data]; // Đóng gói data vào mảng

            fbConnectionApi
                .addConnections(datas)
                .then((response: any) => {
                    if (response.data) {
                        ElMessage({
                            message: t('Successful!'),
                            type: 'success',
                        });
                        context.emit('onChangeView', {
                            viewName: 'ListData',
                            data: null,
                        });
                    } else {
                        ElMessage.error(`Oops, ${response.message}`);
                    }
                })
                .catch((error) => {
                    console.error(error);
                    isLoading.value = false;
                });
            isLoading.value = false;
        }

        function actionEditData(data: any) {
            const id = props.viewSettings.dataItem.id;
            // Giữ lại tất cả các trường khi cập nhật
            fbConnectionApi
                .updateConfig(id, data)
                .then((response: any) => {
                    if (response.data) {
                        ElMessage({
                            message: t('Successful!'),
                            type: 'success',
                        });
                        context.emit('onChangeView', {
                            viewName: 'ListData',
                            data: null,
                        });
                    } else {
                        ElMessage.error(`Oops, ${response.message}`);
                    }
                    isLoading.value = false;
                })
                .catch((error) => {
                    console.error(error);
                    isLoading.value = false;
                });
        }

        const copyToClipboard = async (text: string, key: string) => {
            try {
                await navigator.clipboard.writeText(text);
                copiedKey.value = key;
                ElMessage.success('Đã sao chép!');

                setTimeout(() => {
                    copiedKey.value = null;
                }, 1500);
            } catch (err) {
                ElMessage.error('Không thể sao chép');
            }
        };

        return {
            t,
            isLoading,
            itemModel,
            formRef,
            rules,
            copiedKey,
            viewName, // Bổ sung viewName để sử dụng trong template
            commonFields,
            editOnlyFields,
            onSubmit,
            copyToClipboard,
        };
    },
};