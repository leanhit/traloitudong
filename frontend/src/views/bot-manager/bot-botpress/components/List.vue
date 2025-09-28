<script lang="ts" src="@/scripts/bot-manager/bot-botpress/components/list.ts"></script>

<template>
  <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
    <!-- Header -->
    <div class="card mb-3 shadow-sm">
      <div class="card-body d-flex align-items-center justify-content-between">
        <div class="page-titles">
          <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item active">
              <a href="javascript:void(0)">{{ viewSettings.title }}</a>
            </li>
          </ol>
        </div>
        <el-button size="default" type="primary" @click="refreshDataFn">
          {{ t('Check') }}
        </el-button>
      </div>
    </div>

    <!-- Workspaces -->
    <div v-if="listItems && listItems.length > 0" class="d-flex flex-column gap-4">
      <div
        v-for="(workspace, index) in listItems"
        :key="workspace.id"
        class="card shadow-sm"
      >
        <div class="card-header bg-light d-flex justify-content-between align-items-center">
          <h5 class="mb-0">
            {{ index + 1 }}. {{ workspace.name }}
          </h5>
          <span class="text-muted small">ID: {{ workspace.id }}</span>
        </div>

        <div class="card-body">
          <div class="row mb-3">
            <div class="col-md-4">
              <p><strong>Audience:</strong> {{ workspace.audience }}</p>
            </div>
            <div class="col-md-4">
              <p><strong>Default Role:</strong> {{ workspace.defaultRole }}</p>
            </div>
            <div class="col-md-4">
              <p><strong>Admin Role:</strong> {{ workspace.adminRole }}</p>
            </div>
          </div>

          <!-- Bots -->
          <div class="card mb-3 border">
            <div class="card-header">{{ t('Bots') }}</div>
            <div class="card-body">
              <el-table :data="workspace.bots" border style="width: 100%">
                <el-table-column type="index" label="#" width="50" />
                <el-table-column :label="t('Bot Name')">
                  <template #default="scope">
                    {{ scope.row }}
                  </template>
                </el-table-column>
                <el-table-column label="Actions" width="500">
                  <template #default="scope">
                    <el-button
                      size="small"
                      type="primary"
                      @click="viewBotInfo(scope.row)"
                    >
                      {{ t('View') }}
                    </el-button>
                    <el-button
                      size="small"
                      type="warning"
                      @click="archiveBot(scope.row)"
                    >
                      {{ t('Archive') }}
                    </el-button>
                    <el-button
                      size="small"
                      type="success"
                      @click="unarchiveBot(scope.row)"
                    >
                      {{ t('Unarchive') }}
                    </el-button>
                    <el-button
                      size="small"
                      type="danger"
                      @click="deleteBot(scope.row)"
                    >
                      {{ t('Delete') }}
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>

          <!-- Roles -->
          <div class="card mb-3 border">
            <div class="card-header">{{ t('Roles') }}</div>
            <div class="card-body">
              <div
                v-for="role in workspace.roles"
                :key="role.id"
                class="mb-2 p-2 border rounded"
              >
                <strong>{{ role.id }}</strong> - {{ role.name }}
                <ul class="mt-1 ms-3">
                  <li v-for="(rule, rIndex) in role.rules" :key="rIndex">
                    <code>{{ rule.res }}</code> → <strong>{{ rule.op }}</strong>
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <!-- Pipeline -->
          <div class="card mb-3 border">
            <div class="card-header">{{ t('Pipeline') }}</div>
            <div class="card-body">
              <pre class="mb-0">{{ JSON.stringify(workspace.pipeline, null, 2) }}</pre>
            </div>
          </div>

          <!-- Auth Strategies -->
          <div class="card mb-3 border">
            <div class="card-header">{{ t('Auth Strategies') }}</div>
            <div class="card-body">
              <ul class="mb-0">
                <li v-for="(auth, aIndex) in workspace.authStrategies" :key="aIndex">
                  {{ auth }}
                </li>
              </ul>
            </div>
          </div>

          <!-- Rollout Strategy -->
          <div class="card border">
            <div class="card-header">{{ t('Rollout Strategy') }}</div>
            <div class="card-body">
              <span>{{ workspace.rolloutStrategy }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty -->
    <div v-else class="card shadow-sm">
      <div class="card-body text-center py-5">
        <strong>{{ t('There are no workspaces') }}</strong>
      </div>
    </div>

    <!-- Pagination -->
    <div class="mt-3 d-flex justify-content-end">
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
pre {
  background: #f8f9fa;
  padding: 10px;
  border-radius: 6px;
  font-size: 0.85rem;
}
</style>
