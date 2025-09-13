<script lang="ts" src="@/scripts/create-bot/components/list.ts"></script>

<template>
    <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
        <!-- header -->
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
                        @click="getToken()">
                        <div>{{ t('Get token') }}</div>
                    </el-button>
                    <el-button
                        class="border-0 mx-1 my-1"
                        size="default"
                        type="primary"
                        @click="
                            $emit('onChangeView', {
                                viewName: 'AddBot',
                                data: null,
                            })
                        ">
                        <span class="ml-1">{{ t('Add Bot') }}</span>
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

        <!-- body -->
        <div class="card">
            <div class="row">
                <div class="col-lg-12">
                    <div class="card-body pt-0">
                        <div class="table-responsive rounded card-table">
                             <table class="table table-striped table-bordered w-100">
                              <thead>
                                <tr>
                                  <th style="width:10%">{{ t('Index') }}</th>
                                  <th style="width:15%">{{ t('Bot ID') }}</th>
                                  <th style="width:20%">{{ t('Bot Name') }}</th>
                                  <th style="width:25%">{{ t('Bot Description') }}</th>
                                  <th style="width:15%">{{ t('Created At') }}</th>
                                  <th style="width:15%"  class="text-nowrap text-center">{{ t('Action') }}</th>
                                </tr>
                              </thead>
                              <tbody v-if="listItems && listItems.length > 0">
                                <tr v-for="(itemData, itemIndex) in listItems" :key="itemIndex">
                                  <td class="text-truncate">{{ itemIndex + 1 }}</td>
                                  <td class="text-truncate" :title="itemData.botId">{{ itemData.botId }}</td>
                                  <td class="text-truncate" :title="itemData.botName">{{ itemData.botName }}</td>
                                  <td class="text-truncate" :title="itemData.bot_description">{{ itemData.bot_description }}</td>
                                  <td class="text-truncate" :title="itemData.created_at">{{ formatDateTime(itemData.created_at) }}</td>
                                  <td>
                                    <el-dropdown trigger="click">
                                      <el-button type="primary">
                                        <el-icon :size="15"><More /></el-icon>
                                      </el-button>
                                      <template #dropdown>
                                        <el-dropdown-menu>
                                          <el-dropdown-item>
                                            <el-button size="small" @click="$emit('onChangeView', { viewName: 'EditBot', data: itemData })">
                                              <el-icon :size="15"><Edit /></el-icon> {{ t('Edit Bot') }}
                                            </el-button>
                                          </el-dropdown-item>
                                          <el-dropdown-item>
                                            <el-button size="small" type="danger" @click="deleteBot(itemData.id)">
                                              <el-icon :size="15"><Delete /></el-icon> {{ t('Delete Bot') }}
                                            </el-button>
                                          </el-dropdown-item>
                                        </el-dropdown-menu>
                                      </template>
                                    </el-dropdown>
                                  </td>
                                </tr>
                              </tbody>
                              <tbody v-else>
                                <tr>
                                  <td class="text-center py-5" colspan="6"><strong>{{ t('There are no item') }}</strong></td>
                                </tr>
                              </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- pagination -->
        <div class="mt-2 d-flex justify-content-end">
            <el-pagination
                v-model:current-page="pagePagination.currentPage"
                v-model:page-size="pagePagination.pageSize"
                :page-sizes="[10, 15, 25, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="pagePagination.totalItems"
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
