<script lang="ts" src="@/scripts/profile/components/list.ts"></script>
<template>
    <div class="flex-fill d-flex flex-column w-100 p-2" v-loading="isLoading">
        <!-- header -->
        <div class="d-flex align-items-center justify-content-between">
            <div class="page-titles">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">
                        <a href="javascript:void(0)">{{ t('Profile') }}</a>
                    </li>
                </ol>
            </div>
            <div class="d-flex align-items-center">
                <div class="px-2 w-100"></div>
            </div>
        </div>

        <!-- body -->
        <div class="card">
            <div class="row">
                <div class="col-lg-12">
                    <div class="card-body pt-0">
                        <div class="min-h-screen bg-white">
                            <div
                                class="max-w-4xl mx-auto p-4"
                                v-if="true || user">
                                <h2 class="text-2xl font-bold mb-4">
                                    Hồ sơ cá nhân
                                </h2>
                                <!-- <AvatarCard :user="user" />
                                <AuthInfoForm :user="user" />
                                <AuthContactForm :user="user" /> -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
