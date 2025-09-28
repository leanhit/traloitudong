// client/src/stores/bot.ts (hoặc .js)

import { ref, reactive, computed } from 'vue';
import { defineStore } from 'pinia';
import { botApi } from '@/api/botApi'; // Đảm bảo đường dẫn này đúng

// Kiểu dữ liệu cơ bản cho Role & Rule
interface Rule {
  res: string;
  op: string;
}

interface Role {
  id: string;
  name: string;
  description: string;
  rules: Rule[];
}

interface Pipeline {
  id: string;
  label: string;
  action: string;
  reviewers: string[];
  minimumApprovals: number;
  reviewSequence: string;
}

interface Workspace {
  id: string;
  name: string;
  audience: string;
  bots: string[];
  roles: Role[];
  defaultRole: string;
  adminRole: string;
  pipeline: Pipeline[];
  rolloutStrategy: string;
  authStrategies: string[];
}

export const useWorkspaceStore = defineStore('botpressStore', () => {
  const workspaces = ref<Workspace[]>([]);
  const selectedWorkspace = ref<Workspace | null>(null);
  const isLoading = ref(false);
  const error = ref<string | null>(null);

  /**
   * Fetch workspaces từ API Botpress
   */
  async function fetchWorkspaces() {
    isLoading.value = true;
    error.value = null;
    try {
      const response = await botApi.getWorkspacesFromBotpress();

      console.log("response====>", response);

      // Nếu API trả về mảng trực tiếp
      const rawData = Array.isArray(response) ? response : response.data;

      workspaces.value = rawData.map((ws: any): Workspace => ({
        id: ws.id,
        name: ws.name,
        audience: ws.audience,
        bots: ws.bots || [],
        roles: (ws.roles || []).map((role: any): Role => ({
          id: role.id,
          name: role.name,
          description: role.description,
          rules: (role.rules || []).map((rule: any): Rule => ({
            res: rule.res,
            op: rule.op,
          })),
        })),
        defaultRole: ws.defaultRole,
        adminRole: ws.adminRole,
        pipeline: (ws.pipeline || []).map((pl: any): Pipeline => ({
          id: pl.id,
          label: pl.label,
          action: pl.action,
          reviewers: pl.reviewers || [],
          minimumApprovals: pl.minimumApprovals,
          reviewSequence: pl.reviewSequence,
        })),
        rolloutStrategy: ws.rolloutStrategy,
        authStrategies: ws.authStrategies || [],
      }));


      if (!selectedWorkspace.value && workspaces.value.length > 0) {
        selectedWorkspace.value = workspaces.value[0];
      }

      console.log('Workspaces mapped successfully:', workspaces.value);
      return workspaces.value;
    } catch (err: any) {
      error.value =
        err.response?.data?.message ||
        err.message ||
        'An unexpected error occurred while fetching workspaces.';
      console.error('Error fetching workspaces (catch block):', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  }


  /**
   * Chọn workspace theo id
   */
  function selectWorkspace(workspaceId: string) {
    const ws = workspaces.value.find((w) => w.id === workspaceId);
    if (ws) {
      selectedWorkspace.value = ws;
    } else {
      console.warn(`Workspace with id ${workspaceId} not found`);
    }
  }

  // ============ GETTERS tiện dụng ============
  const roles = computed(() => selectedWorkspace.value?.roles || []);
  const bots = computed(() => selectedWorkspace.value?.bots || []);
  const pipeline = computed(() => selectedWorkspace.value?.pipeline || []);
  const rolloutStrategy = computed(() => selectedWorkspace.value?.rolloutStrategy || null);
  const authStrategies = computed(() => selectedWorkspace.value?.authStrategies || []);
  const defaultRole = computed(() => selectedWorkspace.value?.defaultRole || null);
  const adminRole = computed(() => selectedWorkspace.value?.adminRole || null);

  return {
    workspaces,
    selectedWorkspace,
    isLoading,
    error,
    fetchWorkspaces,
    selectWorkspace,
    // getters
    roles,
    bots,
    pipeline,
    rolloutStrategy,
    authStrategies,
    defaultRole,
    adminRole,
  };
});
