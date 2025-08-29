<script
    lang="ts"
    src="@/scripts/create-connection/components/addConnection.ts"></script>

<template>
    <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
        <div class="d-flex align-items-center justify-content-between pb-3">
            <div class="page-titles">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item">
                        <a href="javascript:void(0)">{{ t('Connection') }}</a>
                    </li>
                    <li class="breadcrumb-item active">
                        <a href="javascript:void(0)">{{
                            viewSettings.title
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
                        ">
                        <div>Back</div>
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
                    label-width="0"
                    class="ruleForm">
                    <div class="row">
                        <div class="col-12">
                            <!-- Các trường chung cho cả 2 chế độ -->
                            <div
                                class="py-2 px-2"
                                v-for="(label, key) in commonFields"
                                :key="key">
                                <strong>
                                    {{ label }}
                                    <a
                                        v-if="
                                            !(
                                                key === 'botName' ||
                                                key === 'fanpageUrl'
                                            )
                                        "
                                        class="text-warning">
                                        ({{ t('Use in connection') }})
                                    </a>
                                    <a
                                        v-if="
                                            key === 'fanpageUrl' &&
                                            itemModel[key]
                                        "
                                        :href="itemModel[key]"
                                        target="_blank"
                                        class="text-primary">
                                        ({{ t('Go to page') }})
                                    </a>
                                    <el-tooltip
                                        v-if="
                                            key === 'verifyToken' ||
                                            key === 'urlCallback' ||
                                            key === 'appId' ||
                                            key === 'appSecret' ||
                                            key === 'pageAccessToken'
                                        "
                                        :content="
                                            copiedKey === key
                                                ? t('Copied!')
                                                : t('Copy')
                                        "
                                        placement="top">
                                        <button
                                            @click="
                                                copyToClipboard(
                                                    itemModel[key],
                                                    key
                                                )
                                            "
                                            type="button"
                                            class="text-primary hover:underline ml-2">
                                            ({{ t('Copy') }})
                                        </button>
                                    </el-tooltip>
                                </strong>
                                <el-form-item :prop="key">
                                    <el-input
                                        v-model="itemModel[key]"
                                        size="large"
                                        :placeholder="label"
                                        />
                                </el-form-item>
                            </div>

                            <!-- Các trường chỉ hiển thị trong chế độ EditConnection -->
                            <div v-if="viewName === 'EditConnection'">
                                <div
                                    class="py-2 px-2"
                                    v-for="(label, key) in editOnlyFields"
                                    :key="key">
                                    <strong>
                                        {{ label }}
                                    </strong>
                                    <el-form-item :prop="key" class="text-nowrap">
                                        <el-switch
                                            v-if="key === 'isEnabled'"
                                            v-model="itemModel.isEnabled"
                                            :active-text="t('Enabled')"
                                            :inactive-text="t('Disabled')">
                                        </el-switch>
                                        <el-input
                                            v-else
                                            v-model="itemModel[key]"
                                            size="large"
                                            :placeholder="label"
                                            disabled />
                                    </el-form-item>
                                </div>
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
                        @click="onSubmit(formRef)">
                        <el-icon>
                            <Plus />
                        </el-icon>
                        <span>{{ viewSettings.title }} </span>
                    </el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
img {
    max-width: 80%;
    height: auto;
}

.enabled-switch-container {
    display: flex;
    align-items: center;

    .el-switch {
        .el-switch__label {
            white-space: nowrap;
        }
    }
}

img {
    max-width: 80%;
    height: auto;
}

// Thêm đoạn CSS này để đảm bảo các nhãn không bị xuống dòng
.el-form-item {
    &.text-nowrap {
        .el-switch {
            .el-switch__label {
                white-space: nowrap;
            }
        }
    }
}
</style>
