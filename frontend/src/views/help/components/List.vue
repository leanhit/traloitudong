<script lang="ts" src="@/scripts/help/components/list.ts"></script>

<template>
  <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
    <!-- header -->
    <div class="d-flex align-items-center justify-content-between">
      <div class="page-titles">
        <ol class="breadcrumb">
          <li class="breadcrumb-item active">
            <a href="javascript:void(0)">Help</a>
          </li>
        </ol>
      </div>
    </div>

    <!-- body -->
    <div class="card mt-2">
      <div class="card-body">
        <h5 class="mb-4">Hướng dẫn sử dụng</h5>
        <div v-for="(topic, index) in helpTopics" :key="index" class="mb-3">
          <h6 class="font-weight-bold">          
          {{ topic.title }}
          <el-button
            v-if="index === 0 "
            class="border-0 mx-1 my-1"
            size="small"
            type="primary"
            @click="$emit('onChangeView', { viewName: 'StepCreate', data: null })"            >
            <span class="ml-1">{{ t("Detail") }}</span>
          </el-button>
          </h6>
          <p class="mb-2 text-muted">{{ topic.content }}</p>
          <el-divider />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card-body {
  font-size: 14px;
}
</style>
