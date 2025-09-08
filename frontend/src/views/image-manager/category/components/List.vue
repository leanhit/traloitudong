<script lang="ts" src="@/scripts/image-manager/category/components/list.ts"></script>

<template>
  <div class="flex-fill d-flex flex-column w-100 px-0" v-loading="isLoading">
    <!-- Header -->
    <div class="d-flex align-items-center justify-content-between">
      <div class="page-titles">
        <ol class="breadcrumb">
          <li class="breadcrumb-item active">
            <a href="javascript:void(0)">{{ viewSettings.title }}</a>
          </li>
        </ol>
      </div>
      <div class="d-flex align-items-center">
        <div class="px-2 w-100">
          <el-button
            class="border-0 mx-1 my-1"
            size="default"
            type="primary"
            @click="
              $emit('onChangeView', {
                viewName: 'AddCategory',
                data: null,
              })
            "
          >
            <span class="ml-1">{{ t('Add Category') }}</span>
          </el-button>
          <el-button size="default" type="primary" @click="refreshDataFn()">
            <div>{{ t('Check') }}</div>
          </el-button>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="card">
      <div class="row">
        <div class="col-lg-12">
          <div class="card-body pt-0">
            <div class="table-responsive rounded card-table">
              <table class="table table-striped table-bordered w-100">
                <thead>
                  <tr>
                    <th class="w-5 text-nowrap">{{ t('Index') }}</th>
                    <th class="w-20 text-nowrap">{{ t('Category Name') }}</th>
                    <th class="w-45 text-nowrap">{{ t('Description') }}</th>
                    <th class="w-20 text-nowrap">{{ t('Created At') }}</th>
                    <th class="w-10 text-nowrap text-center">
                      {{ t('Action') }}
                    </th>
                  </tr>
                </thead>
                <tbody v-if="categoriesList && categoriesList.length > 0">
                  <tr v-for="(itemData, itemIndex) in categoriesList" :key="itemIndex">
                    <td class="text-left">{{ itemIndex + 1 }}</td>
                    <td class="text-left text-truncate" :title="itemData.name">
                      <span class="truncate-text">{{ itemData.name }}</span>
                    </td>
                    <td class="text-left text-truncate" :title="itemData.description">
                      <span class="truncate-text">{{ itemData.description }}</span>
                    </td>
                    <td class="text-left text-truncate" :title="itemData.createdDate">
                      <span class="truncate-text">
                        {{ formatDateTime(itemData.createdDate) }}
                      </span>
                    </td>
                    <td class="text-center text-nowrap">
                      <el-button
                        class="border-0 mx-1 my-1"
                        size="small"
                        :disabled="!itemData"
                        @click="
                          $emit('onChangeView', {
                            viewName: 'EditCategory',
                            data: itemData,
                          })
                        "
                      >
                        <el-icon :size="15" style="vertical-align: middle">
                          <Edit />
                        </el-icon>
                      </el-button>
                      <el-button
                        class="border-0 ml-1"
                        size="small"
                        :disabled="!itemData"
                        @click="deleteCategory(itemData.id)"
                      >
                        <el-icon
                          :size="15"
                          class="text-danger"
                          style="vertical-align: middle"
                        >
                          <Delete />
                        </el-icon>
                      </el-button>
                    </td>
                  </tr>
                </tbody>
                <tbody v-else>
                  <tr>
                    <td class="text-center py-5" colspan="5">
                      <strong>{{ t('There are no item') }}</strong>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Pagination -->
    <div class="mt-2 d-flex justify-content-end">
      <el-pagination
        v-model:current-page="pagePagination.currentPage"
        v-model:page-size="pagePagination.pageSize"
        :page-sizes="[10, 15, 25, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagePagination.totalItems"
        @update:page-size="handleSizeChange"
        @update:current-page="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style scoped>
/* Custom column widths */
.w-5 {
  width: 5% !important;
}
.w-10 {
  width: 10% !important;
}
.w-20 {
  width: 20% !important;
}
.w-45 {
  width: 45% !important;
}

/* Truncate text */
.text-truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.truncate-text {
  display: block;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
