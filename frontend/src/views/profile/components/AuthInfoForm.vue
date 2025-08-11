<template>
    <el-card class="shadow-sm border border-gray-200 rounded-xl">
        <template #header>
            <h2 class="text-lg font-semibold text-gray-800">
                👤 Thông tin cá nhân
            </h2>
        </template>

        <div class="space-y-4">
            <!-- Họ tên -->
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">
                    Họ và tên
                </label>
                <el-input
                    v-model="form.fullName"
                    placeholder="Nhập họ và tên"
                    class="w-full" />
            </div>

            <!-- Ngày sinh -->
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">
                    Ngày sinh
                </label>
                <el-date-picker
                    v-model="form.dateOfBirth"
                    type="date"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    placeholder="Chọn ngày sinh"
                    class="w-full" />
            </div>

            <!-- Giới tính -->
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">
                    Giới tính
                </label>
                <el-select
                    v-model="form.gender"
                    placeholder="Chọn giới tính"
                    class="w-full">
                    <el-option label="Nam" value="MALE" />
                    <el-option label="Nữ" value="FEMALE" />
                    <el-option label="Khác" value="OTHER" />
                </el-select>
            </div>

            <!-- Nút lưu -->
            <div class="text-right mt-4">
                <el-button
                    type="primary"
                    :loading="loading"
                    @click="submit"
                    class="px-6">
                    💾 Cập nhật
                </el-button>
            </div>
        </div>
    </el-card>
</template>

<script lang="ts" src="@/scripts/profile/components/authInfoForm.ts"></script>
