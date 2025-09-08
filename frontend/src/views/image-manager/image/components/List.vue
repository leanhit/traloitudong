<script lang="ts" src="@/scripts/image-manager/image/components/list.ts"></script>

<template>
    <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
        <div class="d-flex align-items-center justify-content-between">
            <div class="page-titles">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">
                        <a href="javascript:void(0)">{{
                            viewSettings.title
                        }}</a>
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
                                viewName: 'AddImage',
                                data: null,
                            })
                        ">
                        <span class="ml-1">{{ t('Add Image') }}</span>
                    </el-button>
                    <el-button
                        size="default"
                        type="primary"
                        @click="refreshDataFn()">
                        <div>{{ t('Check') }}</div>
                    </el-button>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="row">
                <div class="col-lg-12">
                    <div class="card-body pt-0">
                        <div class="table-responsive rounded card-table">
                            <table class="table table-striped table-bordered w-100">
                                <thead>
                                    <tr>
                                        <th style="min-width: 50px;">
                                            {{ t('Index') }}
                                        </th>
                                        <th style="min-width: 80px;">
                                            {{ t('Image') }}
                                        </th>
                                        <th style="min-width: 150px;">
                                            {{ t('Image Name') }}
                                        </th>
                                        <th style="min-width: 250px;">
                                            {{ t('Description') }}
                                        </th>
                                        <th style="min-width: 100px;">
                                            {{ t('Tags') }}
                                        </th>
                                        <th style="min-width: 100px;">
                                            {{ t("Category") }}
                                        </th>
                                        <th style="min-width: 100px;">
                                            {{ t('Created At') }}
                                        </th>
                                        <th style="min-width: 100px;">
                                            {{ t('Action') }}
                                        </th>
                                    </tr>
                                </thead>
                                <tbody v-if="imageStore.imagePagination.content && imageStore.imagePagination.content.length > 0">
                                    <tr v-for="(itemData, itemIndex) in imageStore.imagePagination.content" :key="itemIndex">
                                        <td>
                                            {{ itemIndex + 1 }}
                                        </td>
                                        <td>
                                            <img :src="itemData.fileUrl" alt="Image" class="img-thumbnail" style="width: 60px; height: 60px; object-fit: cover;" />
                                        </td>
                                        <td>
                                            <span class="truncate-text">{{ itemData.title }}</span>
                                        </td>
                                        <td>
                                            <span class="truncate-text">{{ itemData.description }}</span>
                                        </td>
                                        <td>
                                            <span class="truncate-text">{{ itemData.tags ? itemData.tags.join(', ') : '' }}</span>
                                        </td>
                                        <td>
                                            <span class="truncate-text">{{ getCategoryNameById(itemData.categoryId) }}</span>
                                        </td>
                                        <td>
                                            <span class="truncate-text">{{ formatDateTime(itemData.created_at) }}</span>
                                        </td>
                                        <td class=" text-nowrap">                                     
                                            <el-button
                                                class="border-0 mx-1 my-1"
                                                size="small"
                                                :disabled="!itemData"
                                                @click="
                                                    $emit('onChangeView', {
                                                        viewName: 'EditImage',
                                                        data: itemData,
                                                    })">
                                                <el-icon :size="15" style="vertical-align: middle;">
                                                    <Edit />
                                                </el-icon>
                                            </el-button>
                                            <el-button
                                                class="border-0 ml-1"
                                                size="small"
                                                :disabled="!itemData"
                                                @click="deleteImage(itemData.id)">
                                                <el-icon :size="15" class="text-danger" style="vertical-align: middle;">
                                                    <Delete />
                                                </el-icon>
                                            </el-button>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody v-else>
                                    <tr>
                                        <td class="text-center py-5" colspan="8">
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

        <div class="mt-2 d-flex justify-content-end">
            <el-pagination
                v-model:current-page="imageStore.imagePagination.pageNumber"
                v-model:page-size="imageStore.imagePagination.pageSize"
                :page-sizes="[10, 15, 25, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="imageStore.imagePagination.totalElements"
                @update:page-size="handleSizeChange"
                @update:current-page="handleCurrentChange" />
        </div>
    </div>
</template>

<style scoped>
.fixed-table {
    table-layout: fixed;
}
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