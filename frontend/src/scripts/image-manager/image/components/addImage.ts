import { ref, reactive, onMounted, watch } from 'vue';
import type { FormInstance, FormRules, UploadProps, UploadRawFile } from 'element-plus';
import { ElMessage, ElMessageBox } from 'element-plus';
// Giả định bạn có một API client cho ảnh
// import { imageApi } from '@/api/imageApi';
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
    const viewName = ref("");
    const formRef = ref<FormInstance>();

    // Model cho dữ liệu ảnh
    const itemModel = ref({
      id: '',
      name: '',
      description: '',
      url: '', // Dùng cho trường nhập URL trực tiếp hoặc hiển thị URL sau khi upload
      tags: [] as string[], // Mảng các thẻ
      category: '',
      // Thêm trường file để lưu trữ file được chọn từ el-upload
      imageFile: null as File | null,
    });

    // Dùng cho El-Upload preview
    const dialogImageUrl = ref('');
    const dialogVisible = ref(false);

    // Form validation rules
    const rules: FormRules = {
      name: [{ required: true, message: t('Tên ảnh là bắt buộc'), trigger: 'blur' }],
      // Yêu cầu URL hoặc file ảnh phải có
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


    onMounted(async() => {
      viewName.value = props.viewSettings.viewName;
      console.log("viewName", viewName.value);

      await categoryStore.getAllCategories();

      if (viewName.value === 'AddImage') {
        // Reset model khi thêm mới
        itemModel.value = {
          id: '',
          name: '',
          description: '',
          url: '',
          tags: [],
          category: '',
          imageFile: null,
        };
        dialogImageUrl.value = ''; // Xóa preview ảnh cũ
      } else if (viewName.value === 'EditImage') {
        // Gán dữ liệu ảnh hiện có để chỉnh sửa
        const dataItem = props.viewSettings.dataItem;
        itemModel.value = {
          id: dataItem.id || '',
          name: dataItem.name || '',
          description: dataItem.description || '',
          url: dataItem.url || '',
          tags: dataItem.tags || [],
          category: dataItem.category || '',
          imageFile: null, // Không có file khi chỉnh sửa ban đầu
        };
        dialogImageUrl.value = dataItem.url || ''; // Hiển thị ảnh hiện có
      } else {
        console.log("Something went wrong with viewName:", viewName.value);
      }
    });


    // Xử lý khi xóa file khỏi el-upload
    const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
      itemModel.value.imageFile = null;
      dialogImageUrl.value = '';
      // Kích hoạt lại validation cho trường URL/file
      formRef.value?.validateField('url');
    };

    // Xử lý preview ảnh khi click vào ảnh đã upload
    const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
      dialogImageUrl.value = uploadFile.url!;
      dialogVisible.value = true;
    };

    // Xử lý khi URL nhập tay thay đổi
    watch(() => itemModel.value.url, (newUrl) => {
      // Nếu có URL nhập tay, xóa file đã chọn (nếu có)
      if (newUrl) {
        itemModel.value.imageFile = null;
        dialogImageUrl.value = newUrl; // Cập nhật preview theo URL nhập tay
      }
      // Kích hoạt lại validation cho trường URL/file
      formRef.value?.validateField('url');
    });

    // Xử lý gửi form
    function onSubmit(formEl: FormInstance | undefined) {
      isLoading.value = true;
      if (!formEl) return;

      formEl.validate(async (valid) => {
        if (valid) {
          const dataToSend = {
            name: itemModel.value.name,
            description: itemModel.value.description,
            url: itemModel.value.url,
            tags: itemModel.value.tags,
            category: itemModel.value.category,
          };

          try {
            let response: any;
            if (viewName.value === 'AddImage') {
              response = await imageApi.addImage(dataToSend, itemModel.value.imageFile || undefined);
            } else if (viewName.value === 'EditImage') {
              response = await imageApi.updateImage(itemModel.value.id, dataToSend, itemModel.value.imageFile || undefined);
            } else {
              console.log(viewName.value);
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

    watch(() => itemModel.value.url, (newUrl) => {
      if (newUrl) {
        itemModel.value.imageFile = null; // xóa file cũ
        dialogImageUrl.value = newUrl;    // preview theo URL
      }
      formRef.value?.validateField('url');
    });

    const handleFileChange: UploadProps['onChange'] = (uploadFile) => {
      itemModel.value.imageFile = uploadFile.raw || null;
      if (itemModel.value.imageFile) {
        dialogImageUrl.value = URL.createObjectURL(itemModel.value.imageFile);
        itemModel.value.url = ''; // xóa URL nếu chọn file
      } else {
        dialogImageUrl.value = '';
      }
      formRef.value?.validateField('url');
    };


    return {
      t,
      isLoading,
      itemModel,
      formRef,
      rules,
      categories: categoryStore.categories,
      viewName,
      dialogImageUrl,
      dialogVisible,
      handleFileChange,
      handleRemove,
      handlePictureCardPreview,
      onSubmit,
      // copyToClipboard (nếu bạn muốn giữ chức năng copy URL ảnh)
    };
  }
};