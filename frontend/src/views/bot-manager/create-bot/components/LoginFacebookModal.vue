<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useFacebookStore } from "@/stores/facebook";
import { ElMessage } from "element-plus";

const props = defineProps<{ isVisible: boolean }>();
const emit = defineEmits(["close", "login-success"]);

const { t } = useI18n();
const facebookStore = useFacebookStore();

const handleFacebookLogin = () => {
  FB.login(
    (response: fb.StatusResponse) => {
      if (response.authResponse) {
        const { accessToken, userID } = response.authResponse;
        FB.api("/me", { fields: "id,name" }, (userInfo: any) => {
          facebookStore.setFacebookData({ accessToken, userID });
          ElMessage.success(`${t("Login successful")}: ${userInfo.name}`);
          emit("login-success", { accessToken, userID });
          emit("close");
        });
      } else {
        ElMessage.error(t("User cancelled login or did not fully authorize."));
      }
    },
    { scope: "public_profile,email" } // quyền muốn xin
  );
};
</script>

<template>
  <el-dialog v-model="props.isVisible" :title="t('Login Facebook')" width="500px">
    <div class="p-4">
      <el-button type="primary" @click="handleFacebookLogin">
        {{ t("Login with Facebook") }}
      </el-button>
    </div>
    <template #footer>
      <el-button @click="emit('close')">{{ t("Cancel") }}</el-button>
    </template>
  </el-dialog>
</template>
