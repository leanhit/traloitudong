<script lang="ts" src="@/scripts/bot-manager/create-bot/components/addBot.ts"></script>
<template>
    <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
        <div class="d-flex align-items-center justify-content-between pb-3">
            <div class="page-titles">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item">
                        <a href="javascript:void(0)">{{ t('Bot') }}</a>
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
                    label-width="0px"
                    class="ruleForm">
                    <div class="row">
                        <div class="col-12">
                            <div class="py-2 px-2">
                                <strong>
                                    <a>{{ t('Bot Name ') }} </a>
                                    <a class="text-warning"
                                        >({{ t('Require') }})</a
                                    >
                                </strong>
                                <el-form-item prop="botName">
                                    <el-input
                                        v-model="itemModel.botName"
                                        size="large"
                                        placeholder="" />
                                </el-form-item>
                            </div>
                            <div class="py-2 px-2 col">
                                <strong>
                                    <a>{{ t('Bot Description') }}</a>
                                </strong>
                                <el-form-item prop="botDescription">
                                    <el-input
                                        v-model="itemModel.botDescription"
                                        size="large"
                                        placeholder="" />
                                </el-form-item>
                            </div>
                            <div class="py-2 px-2">
                                <strong>
                                    <a>{{ t('Bot Type') }}</a>
                                </strong>
                                <el-form-item prop="bot_type">
                                    <el-select
                                        v-model="itemModel.bot_type"
                                        size="large"
                                        placeholder="Chọn loại Bot">
                                        <el-option
                                            v-for="botType in BOT_TEMPLATES"
                                            :key="botType.id"
                                            :label="botType.name"
                                            :value="botType.id" />
                                    </el-select>
                                </el-form-item>
                            </div>
                            <div class="py-2 px-2" v-if="false && isAddBot">
                                <strong>
                                    <a>{{ t('Bot Id') }}</a>
                                    <el-tooltip
                                        :content="
                                            copiedKey ===
                                            itemModel.botId
                                                ? t('Copied!')
                                                : t('Copy')
                                        "
                                        placement="top">
                                        <button
                                            @click="
                                                copyToClipboard(
                                                    itemModel.botId,
                                                    itemModel.botId
                                                )
                                            "
                                            type="button"
                                            class="text-primary hover:underline ml-2">
                                            ({{ t('Copy') }})
                                        </button>
                                    </el-tooltip>
                                </strong>
                                <el-form-item prop="botId">
                                    <el-input
                                        v-model="itemModel.botId"
                                        size="large"
                                        placeholder="" 
                                        :disable="true"/>
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
</style>
