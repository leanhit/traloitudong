<script lang="ts" src="@/scripts/auto-connect/components/list.ts"></script>

<template>
    <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
        <div class="d-flex align-items-center justify-content-between">
            <div class="page-titles">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">
                        <a href="javascript:void(0)">{{ viewSettings.title }}</a>
                    </li>
                </ol>
            </div>
            <div class="flex flex-col mx-1" style="min-width: 200px;">
                <label class="mb-1 text-sm font-medium text-gray-700 px-2">Chọn Bot ID</label>
                <el-select v-model="selectedBotId" placeholder="Select Bot">
                    <el-option
                    v-for="bot in botIdOptions"
                    :key="bot.value"
                    :label="bot.name"
                    :value="bot.value"
                    />
                </el-select>
            </div>

            <div class="d-flex align-items-center">
                <div class="px-2 w-100">
                    <el-button
                        class="border-0 mx-1 my-1"
                        size="default"
                        type="primary"
                        @click="showFacebookLoginModal()">
                        <span class="ml-1">{{ t('Add Connection') }}</span>
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
                            <table
                                 class="table table-striped table-bordered w-100">
                                <thead>
                                    <tr>
                                        <th style="width: 5%" class="text-nowrap">
                                            {{ t('Index') }}
                                        </th>
                                        <th style="width: 10%" class="text-nowrap">
                                            {{ t('BotID') }}
                                        </th>
                                        <th style="width: 20%" class="text-nowrap">
                                            {{ t('Connection Name') }}
                                        </th>
                                        <th style="width: 20%" class="text-nowrap">
                                            {{ t('Fanpage url') }}
                                        </th>
                                        <th style="width: 10%" class="text-nowrap">
                                            {{ t('Status') }}
                                        </th>
                                        <th style="width: 10%" class="text-nowrap">
                                            {{ t('Created At') }}
                                        </th>
                                        <th style="width: 5%" class="text-nowrap text-center">
                                            {{ t('Action') }}
                                        </th>
                                    </tr>
                                </thead>
                                <tbody v-if="listItems && listItems.length > 0">
                                    <tr
                                        v-for="(itemData, itemIndex) in listItems"
                                        :key="itemIndex">
                                        <td class="text-left">
                                            {{ itemIndex + 1 }}
                                        </td>
                                        <td
                                            class="text-left text-truncate"
                                            :title="itemData.botId">
                                            <span class="truncate-text">{{
                                                itemData.botId
                                            }}</span>
                                        </td>
                                        <td
                                            class="text-left text-truncate"
                                            :title="itemData.botName">
                                            <span class="truncate-text">{{
                                                itemData.botName
                                            }}</span>
                                        </td>
                                        <td
                                            class="text-left text-truncate"
                                            :title="itemData.fanpageUrl">
                                            <span class="truncate-text">
                                                <a :href="itemData.fanpageUrl">{{ itemData.fanpageUrl }}</a>
                                            </span>
                                        </td>
                                        <td
                                            class="text-left text-truncate"
                                            :title="
                                                itemData.isEnabled
                                                    ? t('Enabled')
                                                    : t('Disabled')
                                            ">
                                            <el-form-item class="text-nowrap pt-3">
                                                <el-button
                                                    size="default"
                                                    :type="
                                                        itemData.enabled ? 'success' : 'danger'
                                                    "
                                                    @click="
                                                        toggleStatus(
                                                            itemData,
                                                            !itemData.enabled
                                                        )
                                                    ">
                                                    {{
                                                        itemData.enabled
                                                            ? t('Enabled')
                                                            : t('Disabled')
                                                    }}
                                                </el-button>
                                            </el-form-item>
                                        </td>
                                        <td
                                            class="text-left text-truncate"
                                            :title="itemData.created_at">
                                            <span class="truncate-text">{{
                                                formatDateTime(itemData.created_at)
                                            }}</span>
                                        </td>
                                        <td class="text-center text-nowrap">
                                            <div class="d-flex gap-0">
                                                <el-button
                                                class="border-0"
                                                size="small"
                                                :disabled="!itemData"
                                                @click="
                                                    $emit('onChangeView', {
                                                    viewName: 'EditConnection',
                                                    data: itemData,
                                                    })
                                                "
                                                >
                                                <el-icon :size="15" style="vertical-align: middle;">
                                                    <Edit />
                                                </el-icon>
                                                </el-button>
                                                <el-button
                                                class="border-0"
                                                size="small"
                                                :disabled="!itemData"
                                                @click="deleteConfig(itemData.id)"
                                                >
                                                <el-icon
                                                    :size="15"
                                                    class="text-danger"
                                                    style="vertical-align: middle;"
                                                >
                                                    <Delete />
                                                </el-icon>
                                                </el-button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tbody v-else>
                                    <tr>
                                        <td
                                            class="text-center py-5"
                                            colspan="11">
                                            <strong>{{
                                                t('There are no item')
                                            }}</strong>
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