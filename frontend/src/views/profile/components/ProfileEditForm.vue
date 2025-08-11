<template>
    <form @submit.prevent="submit" class="space-y-4">
        <div>
            <label class="block font-medium">Full Name</label>
            <input v-model="form.fullName" class="input" />
        </div>

        <div>
            <label class="block font-medium">Phone</label>
            <input v-model="form.phone" class="input" />
        </div>

        <div>
            <label class="block font-medium">Address</label>
            <input v-model="form.address" class="input" />
        </div>

        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">
            Cập nhật
        </button>
    </form>
</template>

<script
    lang="ts"
    src="@/scripts/profile/components/profileEditForm.ts"></script>
