<script lang="ts" src="@/scripts/image-manager/category/components/addCategory.ts"></script>

<template>
  <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
    <div class="d-flex align-items-center justify-content-between pb-3">
      <div class="page-titles">
        <ol class="breadcrumb">
          <li class="breadcrumb-item">
            <a href="javascript:void(0)">{{ t('Category') }}</a>
          </li>
          <li class="breadcrumb-item active">
            <a href="javascript:void(0)">{{
              viewSettings.title || (viewName === 'AddCategory' ? t('Add Category') : t('Edit Category'))
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
              <!-- Tên Category -->
              <div class="py-2 px-2">
                <strong>{{ t('Name') }}</strong>
                <el-form-item prop="name">
                  <el-input
                    v-model="itemModel.name"
                    size="large"
                    :placeholder="t('Enter category name')"
                  />
                </el-form-item>
              </div>

              <!-- Mô tả category -->
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
              <Plus v-if="viewName === 'AddCategory'" />
              <Edit v-else-if="viewName === 'EditCategory'" />
            </el-icon>
            <span>{{ viewSettings.title || (viewName === 'AddCategory' ? t('Add Category') : t('Edit Category')) }}</span>
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