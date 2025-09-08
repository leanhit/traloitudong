import { ref, onMounted, watch, computed } from 'vue';
import type { FormInstance, FormRules, UploadProps } from 'element-plus';
import { ElMessage } from 'element-plus';
import { useI18n } from 'vue-i18n';
import { useCategoryStore } from '@/stores/categoryStore';
import { imageApi } from '@/api/imageApi';

export default {
  props: ['viewSettings'],
  emits: ['onChangeView'],
  setup(props: any, context: any) {
    const categoryStore = useCategoryStore();
    const { t } = useI18n();
    const isLoading = ref(false);
    const viewName = ref('');
    const formRef = ref<FormInstance>();

    // Model cho dữ liệu ảnh
    const itemModel = ref({
      id: '',
      name: '', // map sang "title" ở server
      description: '',
      url: '', // map sang "urls" (list)
      tags: [] as string[],
      category: '', // map sang "categoryId"
      imageFile: null as File | null,
    });

    // Dùng cho El-Upload preview
    const dialogImageUrl = ref('');
    const dialogVisible = ref(false);

    // Form validation rules
    const rules: FormRules = {
      name: [{ required: true, message: t('Tên ảnh là bắt buộc'), trigger: 'blur' }],
      url: [
        {
          validator: (rule, value, callback) => {
            if (!value && !itemModel.value.imageFile) {
              callback(new Error(t('Vui lòng cung cấp URL ảnh hoặc tải lên một file.')));
            } else {
              callback();
            }
          },
          trigger: 'blur',
        },
      ],
      tags: [{ type: 'array', message: t('Vui lòng chọn hoặc nhập ít nhất một thẻ'), trigger: 'change' }],
      category: [{ required: true, message: t('Danh mục là bắt buộc'), trigger: 'blur' }],
    };

    onMounted(async () => {
      viewName.value = props.viewSettings.viewName;
      await categoryStore.getAllCategories();

      if (viewName.value === 'AddImage') {
        itemModel.value = {
          id: '',
          name: '',
          description: '',
          url: '',
          tags: [],
          category: '',
          imageFile: null,
        };
        dialogImageUrl.value = '';
      } else if (viewName.value === 'EditImage') {
        const dataItem = props.viewSettings.dataItem;
        itemModel.value = {
          id: dataItem.id || '',
          name: dataItem.title || '',
          description: dataItem.description || '',
          url: dataItem.fileUrl || '',
          tags: dataItem.tags || [],
          category: dataItem.category?.id || '',
          imageFile: null,
        };
        dialogImageUrl.value = dataItem.fileUrl || '';
      }
    });

    // Xử lý xóa file
    const handleRemove: UploadProps['onRemove'] = () => {
      itemModel.value.imageFile = null;
      dialogImageUrl.value = '';
      formRef.value?.validateField('url');
    };

    // Xử lý preview ảnh
    const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
      dialogImageUrl.value = uploadFile.url!;
      dialogVisible.value = true;
    };

    // Xử lý khi nhập URL
    watch(
      () => itemModel.value.url,
      (newUrl) => {
        if (newUrl) {
          itemModel.value.imageFile = null;
          dialogImageUrl.value = newUrl;
        }
        formRef.value?.validateField('url');
      }
    );

    // Xử lý chọn file
    const handleFileChange: UploadProps['onChange'] = (uploadFile) => {
      itemModel.value.imageFile = uploadFile.raw || null;
      if (itemModel.value.imageFile) {
        dialogImageUrl.value = URL.createObjectURL(itemModel.value.imageFile);
        itemModel.value.url = '';
      } else {
        dialogImageUrl.value = '';
      }
      formRef.value?.validateField('url');
    };

    // Submit form

    // Submit form
    async function onSubmit(formEl: FormInstance | undefined) {
      isLoading.value = true;
      if (!formEl) return;

      formEl.validate(async (valid) => {
        if (valid) {
          const formData = new FormData();

          // luôn gửi files, nếu không có thì để rỗng
          if (itemModel.value.imageFile) {
            formData.append('files', itemModel.value.imageFile);
          } else {
            formData.append('files', new Blob([]), ''); // gửi rỗng để tránh null
          }

          // luôn gửi urls, nếu không có thì để chuỗi rỗng
          formData.append('urls', itemModel.value.url || '');

          formData.append('title', itemModel.value.name);
          formData.append('description', itemModel.value.description);
          formData.append('categoryId', itemModel.value.category);

          itemModel.value.tags.forEach((tag) => {
            formData.append('tags', tag);
          });

          try {
            let response: any;
            if (viewName.value === 'AddImage') {
              response = await imageApi.addImage(formData);
            } else if (viewName.value === 'EditImage') {
              response = await imageApi.updateImage(itemModel.value.id, formData);
            } else {
              ElMessage.error(t('Chế độ không hợp lệ!'));
              isLoading.value = false;
              return;
            }

            if (response.data) {
              ElMessage.success(t('Thành công!'));
              context.emit('onChangeView', { viewName: 'ListData', data: null });
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
      categories: computed(() => categoryStore.categories),
      viewName,
      dialogImageUrl,
      dialogVisible,
      handleFileChange,
      handleRemove,
      handlePictureCardPreview,
      onSubmit,
    };
  },
};
