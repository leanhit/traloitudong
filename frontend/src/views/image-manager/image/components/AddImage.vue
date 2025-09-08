<script lang="ts" src="@/scripts/image-manager/image/components/addImage.ts"></script>

<template>
  <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
    <div class="d-flex align-items-center justify-content-between pb-3">
      <div class="page-titles">
        <ol class="breadcrumb">
          <li class="breadcrumb-item">
            <a href="javascript:void(0)">{{ t('Image Manager') }}</a>
          </li>
          <li class="breadcrumb-item active">
            <a href="javascript:void(0)">
              {{ viewSettings.title || (viewName === 'AddImage' ? t('Add Image') : t('Edit Image')) }}
            </a>
          </li>
        </ol>
      </div>
      <div class="d-flex align-items-center">
        <div class="ml-1 mr-4 w-100">
          <el-button
            size="default"
            type="danger"
            class="d-none d-md-block"
            @click="$emit('onChangeView', { viewName: 'ListData', data: null })"
          >
            <div>{{ t('Back') }}</div>
          </el-button>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-body">
        <el-form ref="formRef" :model="itemModel" :rules="rules" label-width="120px" class="ruleForm">
          <div class="row">
            <div class="col-12">
              <!-- Category -->
              <div class="py-2 px-2">
                <strong>{{ t('Category') }}</strong>
                <el-form-item prop="category">
                  <el-select v-model="itemModel.category" :placeholder="t('Select Category')" size="large" style="width: 100%;">
                    <el-option
                      v-for="cat in categories"
                      :key="cat.id"
                      :label="cat.name"
                      :value="cat.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </div>

              <!-- Tên ảnh -->
              <div class="py-2 px-2">
                <strong>{{ t('Name') }}</strong>
                <el-form-item prop="name">
                  <el-input v-model="itemModel.name" size="large" :placeholder="t('Enter image name')" />
                </el-form-item>
              </div>

              <!-- Mô tả -->
              <div class="py-2 px-2">
                <strong>{{ t('Description') }}</strong>
                <el-form-item prop="description">
                  <el-input v-model="itemModel.description" type="textarea" :rows="3" size="large" :placeholder="t('Description')" />
                </el-form-item>
              </div>

              <!-- URL hoặc Upload -->
              <div class="py-2 px-2">
                <strong>{{ t('URL Image Or Upload') }}</strong>
                <el-form-item prop="url">
                  <el-input v-model="itemModel.url" size="large" :placeholder="t('Nhập URL ảnh nếu muốn')" />

                  <div class="text-muted mt-2">{{ t('Hoặc tải ảnh lên:') }}</div>

                  <el-upload
                    class="upload-drag"
                    drag
                    action="#"
                    :auto-upload="false"
                    :file-list="itemModel.imageFile ? [{ name: itemModel.imageFile.name, url: dialogImageUrl }] : []"
                    :limit="1"
                    :on-change="handleFileChange"
                    :on-remove="handleRemove"
                    :on-preview="handlePictureCardPreview"
                  >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">{{ t('Kéo thả ảnh vào đây hoặc nhấn để chọn') }}</div>
                    <div class="el-upload__tip text-muted" style="font-size: 12px;">{{ t('Chỉ chấp nhận 1 ảnh') }}</div>
                  </el-upload>

                  <el-dialog v-model="dialogVisible" width="50%">
                    <img w-full :src="dialogImageUrl" alt="Preview Image" />
                  </el-dialog>
                </el-form-item>
              </div>

              <!-- Tags -->
              <div class="py-2 px-2">
                <strong>{{ t('Tags') }}</strong>
                <el-form-item prop="tags">
                  <el-select
                    v-model="itemModel.tags"
                    multiple
                    filterable
                    allow-create
                    default-first-option
                    :reserve-keyword="false"
                    :placeholder="t('Chọn hoặc nhập thẻ')"
                    size="large"
                    style="width: 100%;"
                  />
                </el-form-item>
              </div>
            </div>
          </div>
        </el-form>
      </div>
      <div class="card-footer">
        <div class="text-center py-3">
          <el-button size="large" type="primary" class="mr-1 ml-1" @click="onSubmit(formRef)">
            <span>{{ viewSettings.title || (viewName === 'AddImage' ? t('Add Image') : t('Update Image')) }}</span>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
/* Các style tùy chỉnh */
.page-titles {
  .breadcrumb {
    background: none;
    padding: 0;
    margin-bottom: 0;
    .breadcrumb-item {
      a {
        text-decoration: none;
        color: inherit;
      }
      &.active a {
        font-weight: bold;
      }
    }
  }
}

.card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.card-body {
  padding: 20px;
}

.card-footer {
  border-top: 1px solid #ebeef5;
  padding: 20px;
  background-color: #f5f7fa;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}

.el-form-item {
  margin-bottom: 20px;
}

img {
  max-width: 100%;
  height: auto;
  display: block;
}

.upload-drag {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  padding: 40px 20px;
  text-align: center;
  cursor: pointer;
  transition: border-color 0.3s, background-color 0.3s;

  &:hover {
    border-color: #409eff;
    background-color: #f0f7ff;
  }

  .el-upload__icon {
    font-size: 36px;
    color: #409eff;
  }

  .el-upload__text {
    margin-top: 10px;
    font-weight: 500;
    color: #606266;
  }

  .el-upload__tip {
    margin-top: 5px;
  }
}

</style>