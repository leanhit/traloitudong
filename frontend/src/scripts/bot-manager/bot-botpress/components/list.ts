import { useI18n } from 'vue-i18n';
import { reactive, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { splitData } from '@/until/search';
import { exportDataAsJson } from '@/until/writeFile';
import { useWorkspaceStore } from '@/stores/botpressStore';
import { botApi } from '@/api/botApi';

export default {
  props: ['viewSettings'],
  setup() {
    const { t } = useI18n();
    const workspaceStore = useWorkspaceStore();

    const pagePagination = reactive({
      pageSize: 10,
      currentPage: 1,
      totalItems: 0,
    });

    const listItems = computed(() =>
      splitData(workspaceStore.workspaces, pagePagination)
    );

    async function refreshDataFn() {
      try {
        const data = await workspaceStore.fetchWorkspaces();
        //exportDataAsJson(data, 'workspaces.json');
        pagePagination.totalItems = data.length;
      } catch (err) {
        console.error('❌ Error fetching workspaces:', err);
        ElMessage.error(t('Error fetching workspaces'));
      }
    }

    function selectWorkspace(workspace: any) {
      workspaceStore.selectedWorkspace = workspace;
    }

    function viewBotInfo(botId: string) {
      console.log("🔍 View bot:", botId);
      botApi.getBotInfoFromBotpress(botId)
    }

    async function archiveBot(botId: string) {
      try {
        await botApi.archiveBot(botId);
        ElMessage.success(t("Bot archived successfully"));
        refreshDataFn();
      } catch (err) {
        ElMessage.error(t("Failed to archive bot"));
      }
    }

    async function unarchiveBot(botId: string) {
      try {
        await botApi.unarchiveBot(botId);
        ElMessage.success(t("Bot unarchived successfully"));
        refreshDataFn();
      } catch (err) {
        ElMessage.error(t("Failed to unarchive bot"));
      }
    }

    async function deleteBot(botId: string) {
      try {
        await botApi.deleteBot(botId);
        ElMessage.success(t("Bot deleted successfully"));
        refreshDataFn();
      } catch (err) {
        ElMessage.error(t("Failed to delete bot"));
      }
    }


    onMounted(() => {
      refreshDataFn();
    });

    const handleSizeChange = (size: number) => {
      pagePagination.pageSize = size;
    };

    const handleCurrentChange = (page: number) => {
      pagePagination.currentPage = page;
    };

    return {
      t,
      pagePagination,
      handleSizeChange,
      handleCurrentChange,
      refreshDataFn,
      selectWorkspace,
      isLoading: workspaceStore.isLoading,
      listItems,
      selectedWorkspace: workspaceStore.selectedWorkspace,
      roles: workspaceStore.roles,
      bots: workspaceStore.bots,
      pipeline: workspaceStore.pipeline,
      authStrategies: workspaceStore.authStrategies,
      rolloutStrategy: workspaceStore.rolloutStrategy,
      viewBotInfo,
      archiveBot,
      unarchiveBot,
      deleteBot,
    };
  },
};

