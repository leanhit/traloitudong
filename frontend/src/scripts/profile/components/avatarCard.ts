import { ref, computed, watch, onBeforeUnmount } from 'vue';
import { usersApi } from '@/api/usersApi';
import { useAuthStore } from '@/stores/auth';
import { ElMessage } from 'element-plus';
import { useI18n } from 'vue-i18n';

export default {
    props: {
        user: {
            type: Object,
            default: () => ({}),
        },
    },
    setup(props) {
        const { t } = useI18n();
        const authStore = useAuthStore();

        const selectedFile = ref<File | null>(null);
        const selectedFileName = ref('');
        const isUploading = ref(false);

        const userAvatar = ref(
            props.user?.avatar || 'https://via.placeholder.com/80x80?text=Avatar'
        );

        const objectUrl = ref<string | null>(null);

        // Tạo preview URL khi file thay đổi
        watch(selectedFile, (newFile) => {
            if (objectUrl.value) {
                URL.revokeObjectURL(objectUrl.value);
                objectUrl.value = null;
            }
            if (newFile) {
                objectUrl.value = URL.createObjectURL(newFile);
            }
        });

        // Dọn dẹp objectURL khi unmount
        onBeforeUnmount(() => {
            if (objectUrl.value) {
                URL.revokeObjectURL(objectUrl.value);
            }
        });

        const previewAvatar = computed(() => {
            return objectUrl.value || userAvatar.value;
        });

        // Hàm xử lý khi chọn file
        const onFileChange = (file: any) => {
            if (!file) {
                selectedFile.value = null;
                selectedFileName.value = '';
                return;
            }

            if (file.raw instanceof File) {
                selectedFile.value = file.raw;
                selectedFileName.value = file.name || file.raw.name || '';
            } else if (file instanceof File) {
                selectedFile.value = file;
                selectedFileName.value = file.name || '';
            } else {
                selectedFile.value = null;
                selectedFileName.value = '';
                console.warn('Invalid file input in onFileChange:', file);
            }
        };

        const resetUploadState = () => {
            selectedFile.value = null;
            selectedFileName.value = '';
            if (objectUrl.value) {
                URL.revokeObjectURL(objectUrl.value);
                objectUrl.value = null;
            }
        };

        const submitAvatar = async () => {
            if (!selectedFile.value) {
                ElMessage.warning('Vui lòng chọn ảnh trước');
                return;
            }

            try {
                isUploading.value = true;
                const formData = new FormData();
                formData.append('avatar', selectedFile.value);

                const response = await usersApi.updateAvatar(formData);
                ElMessage.success('🎉 Avatar đã được cập nhật!');
                const newAvatarUrl = response.data.avatar;

                userAvatar.value = newAvatarUrl;
                authStore.updateUserProfile({ avatar: newAvatarUrl });
                resetUploadState();
            } catch (err) {
                console.error(err);
                ElMessage.error('❌ Lỗi khi cập nhật avatar');
            } finally {
                isUploading.value = false;
            }
        };

        return {
            t,
            selectedFile,
            selectedFileName,
            isUploading,
            previewAvatar,
            onFileChange,
            submitAvatar,
        };
    },
};