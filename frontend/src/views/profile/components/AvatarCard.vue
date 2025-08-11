<template>
    <el-card class="mb-6">
        <template #header>
            <h2 class="text-lg font-semibold text-gray-800">🖼️ Ảnh đại diện</h2>
        </template>
        <div class="flex items-center justify-between">
            <!-- Avatar preview + Upload -->
            <div class="flex items-center gap-4">
                <!-- Preview -->
                <el-image
                    :src="previewAvatar"
                    fit="cover"
                    class="w-40 h-40 rounded-full border" />

                <!-- Upload -->
                <el-upload
                    class="avatar-uploader"
                    :auto-upload="false"
                    :show-file-list="false"
                    :on-change="onFileChange"
                    accept="image/*">
                    <el-button type="primary" plain>Chọn ảnh</el-button>
                </el-upload>

                <div class="text-sm text-gray-500" v-if="selectedFileName">
                    {{ selectedFileName }}
                </div>
            </div>

            <!-- Submit -->
            <el-button
                type="primary"
                :loading="isUploading"
                :disabled="!selectedFile"
                @click="submitAvatar"
                class="px-6">
                💾 Cập Nhật
            </el-button>
        </div>
    </el-card>
</template>

<script lang="ts" src="@/scripts/profile/components/avatarCard.ts"></script>
