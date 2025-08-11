<script lang="ts" src="@/scripts/image-manager/components/addImage.ts"></script>

<template>
  <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
    <div class="d-flex align-items-center justify-content-between pb-3">
      <div class="page-titles">
        <ol class="breadcrumb">
          <li class="breadcrumb-item">
            <a href="javascript:void(0)">{{ t('Image Manager') }}</a>
          </li>
          <li class="breadcrumb-item active">
            <a href="javascript:void(0)">{{
              viewSettings.title || (viewName === 'AddImage' ? t('Add Image') : t('Edit Image'))
            }}</a>
          </li>
        </ol>
      </div>
      <div class="d-flex align-items-center">
        <div class="ml-1 mr-4 w-100">
          <el-button
            size="default"
            type="danger"
            class="d-none d-md-block"
            @click="
              $emit('onChangeView', {
                viewName: 'ListData',
                data: null,
              })
            "
          >
            <div>{{ t('Back') }}</div>
          </el-button>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-body">
        <el-form
          ref="formRef"
          :model="itemModel"
          :rules="rules"
          label-width="120px"
          class="ruleForm"
        >
          <div class="row">
            <div class="col-12">
              <!-- Tên ảnh -->
              <div class="py-2 px-2">
                <strong>{{ t('Name') }}</strong>
                <el-form-item prop="name">
                  <el-input
                    v-model="itemModel.name"
                    size="large"
                    :placeholder="t('Enter image name')"
                  />
                </el-form-item>
              </div>

              <!-- Mô tả ảnh -->
              <div class="py-2 px-2">
                <strong>{{ t('Description') }}</strong>
                <el-form-item prop="description">
                  <el-input
                    v-model="itemModel.description"
                    type="textarea"
                    :rows="3"
                    size="large"
                    :placeholder="t('Description')"
                  />
                </el-form-item>
              </div>

              <!-- URL ảnh (hoặc Upload) -->
              <div class="py-2 px-2">
                <strong>{{ t('URL Image Or Upload') }}</strong>
                <el-form-item prop="url">
                  <el-input
                    v-model="itemModel.url"
                    size="large"
                    :placeholder="t('Image URL')"
                  />
                  <div class="text-muted mt-2">
                    {{ t('Or Upload An Image:') }}
                  </div>
                  <el-upload
                    action="#"
                    list-type="picture-card"
                    :auto-upload="false"
                    :on-change="handleFileChange"
                    :on-remove="handleRemove"
                    :on-preview="handlePictureCardPreview"
                    :limit="1"
                    :file-list="itemModel.imageFile ? [{ name: itemModel.imageFile.name, url: dialogImageUrl }] : []"
                  >
                    <el-icon><Plus /></el-icon>
                  </el-upload>
                  <el-dialog v-model="dialogVisible">
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
                  >
                    <!-- Bạn có thể thêm các option mặc định nếu muốn -->
                    <!-- <el-option label="Mèo" value="mèo"></el-option> -->
                  </el-select>
                </el-form-item>
              </div>

              <!-- Category -->
              <div class="py-2 px-2">
                <strong>{{ t('Category') }}</strong>
                <el-form-item prop="category">
                  <el-select
                    v-model="itemModel.category"
                    :placeholder="t('Select Category')"
                    size="large"
                    style="width: 100%;"
                  >
                    <el-option
                      v-for="cat in categories"
                      :key="cat"
                      :label="cat"
                      :value="cat"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </div>
          </div>
        </el-form>
      </div>
      <div class="card-footer">
        <div class="text-center py-3">
          <el-button
            size="large"
            type="primary"
            class="mr-1 ml-1"
            @click="onSubmit(formRef)"
          >
            <el-icon>
              <Plus v-if="viewName === 'AddImage'" />
              <Edit v-else-if="viewName === 'EditImage'" />
            </el-icon>
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
</style>