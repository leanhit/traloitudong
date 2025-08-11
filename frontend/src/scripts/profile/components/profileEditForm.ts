import { ref, watch } from 'vue';
import { usersApi } from '@/api/usersApi';
import { ElMessage } from 'element-plus';

export default {
    props: {
        user: {
            type: Object,
            default: () => ({}),
        },
    },
    setup(props: any) {
        const fullName = ref('');
        const phone = ref('');
        const address = ref('');

        // Đồng bộ props.user -> form
        watch(
            () => props.user,
            (val) => {
                if (val) {
                    fullName.value = val.info?.fullName || '';
                    phone.value = val.contact?.phone || '';
                    address.value = val.contact?.address || '';
                }
            },
            { immediate: true }
        );

        const submit = async () => {
            try {
                await usersApi.updateProfile({
                    fullName: fullName.value,
                    phone: phone.value,
                    address: address.value,
                });
                ElMessage.success('Cập nhật thành công');
                emit('updated');
            } catch (err) {
                console.error(err);
                ElMessage.error('Đã xảy ra lỗi khi cập nhật');
            }
        };

        return {
            fullName,
            phone,
            address,
            submit,
        };
    },
};