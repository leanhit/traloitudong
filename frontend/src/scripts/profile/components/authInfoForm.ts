import { reactive, watch, ref } from 'vue';
import { usersApi } from '@/api/usersApi';
import { useAuthStore } from '@/stores/auth';
import { ElMessage } from 'element-plus';
import { useI18n } from 'vue-i18n';

export default {
    props: ['user'],
    setup(props: any) {
        const authStore = useAuthStore();
        const loading = ref(false);
        const { t } = useI18n();

        const form = reactive({
            fullName: '',
            dateOfBirth: '',
            gender: '',
        });

        watch(
            () => props.user,
            (val) => {
                if (val) {
                    form.fullName = val.fullName || '';
                    form.dateOfBirth = val.dateOfBirth || '';
                    form.gender = val.gender || '';
                }
            },
            { immediate: true }
        );

        const submit = async () => {
            loading.value = true;
            try {
                const res = await usersApi.updateProfile({
                    fullName: form.fullName,
                    dateOfBirth: form.dateOfBirth,
                    gender: form.gender,
                });

                if (res.status !== 200) {
                    ElMessage.error('❌ Lỗi khi cập nhật thông tin cá nhân');
                    return;
                } else {
                    ElMessage.success('✅ Thông tin cá nhân đã được cập nhật!');
                    authStore.updateUserProfile({
                        fullName: form.fullName,
                        dateOfBirth: form.dateOfBirth,
                        gender: form.gender,
                    });
                }
            } catch (err) {
                console.error(err);
                ElMessage.error('❌ Đã xảy ra lỗi khi cập nhật!');
            } finally {
                loading.value = false;
            }
        };

        return {
            t,
            props,
            loading,
            form,
            submit,
        };
    },
};