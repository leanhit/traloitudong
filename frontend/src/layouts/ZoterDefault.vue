<script lang="ts">
import { defineAsyncComponent, ref, watch } from 'vue';
import SkeletonBox from '@/components/SkeletonBox.vue';

export default {
    components: {
        Navbar: defineAsyncComponent({
            loader: () => import('@/views/defaultLayout/Navbar.vue'),
            loadingComponent: SkeletonBox,
        }),
        Sidebar: defineAsyncComponent({
            loader: () => import('@/views/defaultLayout/Sidebar.vue'),
            loadingComponent: SkeletonBox,
        }),
        Footer: defineAsyncComponent({
            loader: () => import('@/views/defaultLayout/Footer.vue'),
            loadingComponent: SkeletonBox,
        }),
    },
    setup() {
        const isShowSidebar = ref(false);
        const toggerSidebar = () => {
            isShowSidebar.value = !isShowSidebar.value;
        }
        if(window.innerWidth <= 720){
            isShowSidebar.value = false;
        }
        return {
            isShowSidebar,
            toggerSidebar
        }
    }
}
</script>
<template>
    <div>
        <!-- Begin page -->
        <div id="wrapper bg-secondary" >
            <!-- ========== Left Sidebar Start ========== -->
            <div :class="{ hideDiv: isShowSidebar }">
                <Sidebar />
            </div>
            <!-- Left Sidebar End -->

            <!-- Start right Content here -->
            <div class="content-page " :class="{ fullWidth: isShowSidebar }">
                <!-- Start content -->
                <div class="content">
                    <div class=" d-none d-md-block showBtn">
                        <el-button @click="isShowSidebar = !isShowSidebar">
                            <span v-if="isShowSidebar"> <el-icon><ArrowRightBold  /></el-icon>  </span>
                            <span v-else-if="!isShowSidebar"> <el-icon><ArrowLeftBold /></el-icon></span>
                        </el-button>
                    </div>
                    <div class="d-md-none">
                        <!-- {{ isShowSidebar = !isShowSidebar }} -->
                    </div>
                    <!-- Top Bar Start -->
                    <div class="topbar">
                        <Navbar />
                    </div>
                    <!-- Top Bar End -->

                    <div>
                        <router-view />
                    </div>
                    <!-- Page content Wrapper -->

                </div> <!-- content -->

                <Footer />

            </div>
            <!-- End Right content here -->

        </div>
        <!-- END wrapper -->
    </div>
</template>

<style scoped>
.fullWidth {
    width: 100%;
    margin-left: 0;
}

.hideDiv {
    display: none;
}

.showBtn{
    position: fixed;
    margin-top:0px;
    margin-left: 0px;
    border: 0;
    background-color: none;
    ;
}
</style>