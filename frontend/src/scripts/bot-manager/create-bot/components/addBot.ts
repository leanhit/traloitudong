import { ref, reactive, onMounted } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { ElMessage, ElMessageBox } from 'element-plus';
import { botApi } from '@/api/botApi';
import { useI18n } from 'vue-i18n';
import { BOT_TEMPLATES } from '@/until/constant'; // Đảm bảo đường dẫn này đúng

export default {
    props: ['viewSettings'],
    emits: ['onChangeView'],
    setup(props: any, context: any) {
        const { t } = useI18n();
        const menu = ref({});
        const isLoading = ref(false);
        const viewName = ref('');
        const formRef = ref<FormInstance>();
        const copiedKey = ref<string | null>(null); // Khai báo rõ ràng kiểu dữ liệu
        const isAddBot = ref(false);

        const itemModel = ref({
            id: '',
            botName: '',
            botId: '',
            bot_type: '',
            bot_description: '',
            created_at: '',
        });

        // Form validation rules
        const rules: FormRules = {
            // Đảm bảo kiểu FormRules
            botId: [
                { required: false, message: '', trigger: 'blur' }, // Có thể xem xét trigger: 'change' nếu bạn muốn validate ngay khi giá trị thay đổi.
            ],
            botName: [
                {
                    required: true,
                    message: 'Bot name là bắt buộc',
                    trigger: 'blur',
                },
            ],
            bot_type: [
                // Đã thêm quy tắc cho bot_type
                {
                    required: false,
                    message: 'Loại Bot là bắt buộc',
                    trigger: 'change', // Trigger 'change' phù hợp hơn cho select
                },
            ],
            bot_description: [
                // Thêm quy tắc cho bot_description nếu cần
                { required: false, message: '', trigger: 'blur' },
            ],
        };

        onMounted(() => {
            viewName.value = props.viewSettings.viewName;

            console.log('viewName', viewName.value);

            if (viewName.value === 'AddBot') {
                isAddBot.value = true;
                itemModel.value.id = '';
                // Đặt giá trị mặc định cho bot_type khi thêm mới nếu cần
                // itemModel.value.bot_type = BOT_TEMPLATES.length > 0 ? BOT_TEMPLATES[0].value : '';
            } else if (
                viewName.value === 'EditBot' ||
                viewName.value === 'CloneBot'
            ) {
                isAddBot.value = false;
                itemModel.value = props.viewSettings.dataItem;
                // Đảm bảo itemModel.value.bot_type tồn tại trong BOT_TEMPLATES
                // Nếu không, có thể chọn một giá trị mặc định để tránh lỗi hiển thị
            } else {
                console.log('Something went wrong')!;
            }
        });

        function onSubmit(formEl: FormInstance | undefined) {
            isLoading.value = true;

            console.log('itemModel', itemModel.value);
            if (!formEl) {
                isLoading.value = false; // Đảm bảo isLoading được đặt lại
                return;
            }
            formEl.validate((valid) => {
                if (valid) {
                    const data = {
                        ...itemModel.value,
                    };

                    if (viewName.value === 'AddBot') {
                        // Sử dụng === thay vì ==
                        actionAddData(data);
                    } else if (viewName.value === 'EditBot') {
                        actionEditData(data);
                    } else {
                        console.log(viewName.value);
                        isLoading.value = false; // Đảm bảo isLoading được đặt lại
                    }
                } else {
                    console.log('error submit!');
                    isLoading.value = false;
                }
            });
        }

        function actionAddData(data: any) {
            botApi
                .addBot(data)
                .then((response: any) => {
                    if (response.data) {
                        console.log("create bot response ===>",response.data);
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
                    ElMessage.error(
                        t('An error occurred during add operation.')
                    ); // Thêm thông báo lỗi chung
                })
                .finally(() => {
                    // Sử dụng finally để đảm bảo isLoading luôn được đặt lại
                    isLoading.value = false;
                });
        }

        function actionEditData(data: any) {
            const id = ref(props.viewSettings.dataItem.id);
            botApi
                .updateBot(id.value, data)
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
                    ElMessage.error(
                        t('An error occurred during update operation.')
                    ); // Thêm thông báo lỗi chung
                })
                .finally(() => {
                    // Sử dụng finally để đảm bảo isLoading luôn được đặt lại
                    isLoading.value = false;
                });
        }

        const copyToClipboard = async (text: string, key: string) => {
            // Khai báo rõ ràng kiểu dữ liệu
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
            menu,
            copiedKey,
            onSubmit,
            copyToClipboard,
            BOT_TEMPLATES, // Đảm bảo BOT_TEMPLATES được trả về để template có thể sử dụng
            isAddBot
        };
    },
};
