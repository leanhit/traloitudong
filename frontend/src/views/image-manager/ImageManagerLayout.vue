<template>
  <div class="image-manager-layout">
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'ImageManagerLayout',
});
</script>

<style scoped>
.image-manager-layout {
  /* Bạn có thể thêm các style layout chung cho các trang con tại đây nếu cần */
  /* Ví dụ: padding, margin, flexbox layout, v.v. */
  height: 100%;
  width: 100%;
}
</style>