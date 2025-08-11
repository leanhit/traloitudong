import { reactive, watch, ref } from 'vue';
import { useAddressStore } from '@/stores/addressStore';
import { useAuthStore } from '@/stores/auth';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';
import { usersApi } from '@/api/usersApi';
import { ElMessage } from 'element-plus';

export default {
    props: ['user'],
    setup(props) {
        const { t } = useI18n();
        const addressStoreInstance = useAddressStore();
        const { provinces, districts, wards } =
            storeToRefs(addressStoreInstance);
        const { fetchDistricts, fetchWards } = addressStoreInstance;
        const loading = ref(false);
        const authStore = useAuthStore();

        const form = reactive({
            phone: '',
            province: '',
            district: '',
            ward: '',
            detail: '',
        });

        watch(
            () => props.user,
            (val) => {
                if (val) {
                    form.phone = val.phone || '';
                    form.province = val.province || '';
                    form.district = val.district || '';
                    form.ward = val.ward || '';
                    form.detail = val.detail || '';
                }
            },
            { immediate: true }
        );

        const onProvinceChange = (code: string) => {
            form.district = '';
            form.ward = '';
            districts.value = [];
            wards.value = [];
            if (code) fetchDistricts(code);
        };

        const onDistrictChange = (code: string) => {
            form.ward = '';
            wards.value = [];
            if (code) fetchWards(code);
        };

        const submit = async () => {
            loading.value = true;
            try {
                const res = await usersApi.updateProfile({
                    phone: form.phone,
                    province: form.province,
                    district: form.district,
                    ward: form.ward,
                    detail: form.detail,
                });

                if (res.status !== 200) {
                    ElMessage.error('❌ Lỗi khi cập nhật thông tin liên hệ');
                    return;
                } else {
                    ElMessage.success('✅ Thông tin liên hệ đã được cập nhật!');
                    authStore.updateUserProfile({ ...form }); // update toàn bộ thông tin
                }
            } catch (err) {
                console.error(err);
                ElMessage.error('❌ Lỗi khi cập nhật thông tin liên hệ');
            } finally {
                loading.value = false;
            }
        };

        return {
            t,
            loading,
            form,
            provinces,
            districts,
            wards,
            onProvinceChange,
            onDistrictChange,
            submit,
        };
    },
};