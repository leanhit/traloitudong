<script lang="ts" src="@/scripts/help/index.ts"></script>
<template>
    <div class="box-card w-100 d-flex flex-fill flex-column"
        v-loading="isChangeViewLoading"
		:body-style="{ padding: '0.5rem 0.8rem' }">
        <component
            :is="currentComponent"
            @onChangeView="changeView"
            :viewSettings="viewSettings"
        ></component>
    </div>
</template>