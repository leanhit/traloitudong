<!-- <script setup lang="ts">
import { ref, onMounted, watch, computed, inject } from 'vue';
import NotificationDropdown from './NotificationDropdown.vue';
import AccountDropdown from "@/components/AccountDropdown.vue";
import { RouterLink, RouterView, useRoute } from 'vue-router';
import { navItems } from '@/helpers/navigationItems';
import { useDataconnectionStore } from '@/stores/dataCategory';
import { useAppStateStore } from "@/stores/appState";
import SecurityHelper from '@/helpers/securityHelper';
import { debounce } from 'lodash';
const currentOrg = import.meta.env.VITE_APP_ORG;
const appState = useAppStateStore();
const securityHelper = new SecurityHelper();
const $emitter: any = inject('$emitter');
const isPageLoading = ref(false);
var togglePageLoadingHide = debounce(() => {
    isPageLoading.value = !isPageLoading.value;
}, 1000);
const currentUser: any = computed(() => appState.$state.userInfo);
securityHelper.isLoggedIn().then(isLogined => {
    if (isLogined !== null && isLogined === true) {
        securityHelper.getUser()
            .then((user: any) => {
                if (user) {
                    localStorage.setItem(securityHelper.getAccessTokenStoreName(), user.access_token);
                    appState.setUserInfo({
                        ...user.profile,
                        accessToken: user.access_token
                    });
                    securityHelper.getAccountService().then((userProfileUrl: string) => {
                        if (userProfileUrl !== null && userProfileUrl !== '') {
                            appState.setUserProfileUrl(userProfileUrl);
                        }
                    });
                }
            })
            .catch(error => {
                console.error(error);
            });
    }
}).catch(error => {
    console.error(error);
});

const route = useRoute();
const dataconnectionStore = useDataconnectionStore();
dataconnectionStore.getOrganization();
dataconnectionStore.getAppParams();
const linkTime = ref(new Date().getTime());
const toggleMenu = (navItem: any) => {
    if (navItem.hasOwnProperty('toggleSubMenu')) {
        navItem.toggleSubMenu = !navItem.toggleSubMenu;
    }
    else {
        navItem.toggleSubMenu = true;
    }
};
const navItemsRef = ref(navItems);
watch(() => route.path, (newValRoutePath) => {
    var foundedNavs = navItemsRef.value.find(xNav => {
        return xNav.toggleSubMenu && xNav.url && xNav.url !== '/' && !newValRoutePath.startsWith(xNav.url);
    });
    if (foundedNavs && foundedNavs !== null) {
        foundedNavs.toggleSubMenu = false;
    }
});

const onComponentLoading = () => { 
    //togglePageLoadingHide();
    isPageLoading.value = true;
};
const onComponentLoaded = () => { 
    togglePageLoadingHide();
    //isPageLoading.value = false;
};

if ($emitter) {
    $emitter.on('asyncComponentLoading', onComponentLoading);
    $emitter.on('asyncComponentLoaded', onComponentLoaded);
}

onMounted(() => {
    var foundedNav = navItemsRef.value.find(xNav => {
        return xNav && xNav.url && xNav.url !== '/' && route.path.startsWith(xNav.url);
    });
    if (foundedNav && foundedNav !== null) {
        foundedNav.toggleSubMenu = true;
    }
    // setInterval(() => {
    //     linkTime.value = new Date().getTime();
    // }, 1000);
});
</script>
<template>
    <div class="wrapper w-100 position-relative">
        <div class="page-loading-indicator d-flex align-items-center justify-content-center" v-if="isPageLoading">
            <div class="text-center">
                <!-- <div class="mt-3">
                        <img src="/custom-img/loading-vector.svg" style="width: 100%; max-width: 30rem; min-width: 400px;" />
                    </div> -->
                <div class="mt-3 d-flex align-items-center justify-content-center">
                    <div>
                        <div class="lds-loading color-white">
                            <div></div>
                            <div></div>
                            <div></div>
                        </div>
                    </div>
                    <h4 class="text-left text-white ml-3">
                        Đang tải dữ liệu..
                    </h4>
                </div>
            </div>
        </div>
        <!-- ./wrapper -->
        <!-- Navbar -->
        <nav class="main-header navbar navbar-expand bg-header-color d-flex align-items-center">
            <!-- Left navbar links -->
            <ul class="navbar-nav d-flex align-items-center">
                <li class="nav-item d-inline-block">
                    <a class="nav-link text-white nav-bars-btn" data-widget="pushmenu" href="#" role="button"><i
                            class="fas fa-bars text-white"></i></a>
                </li>
                <li class="menu-on-sidebar-closed">
                    <div class="d-flex align-items-center ">
                        <div>
                            <img src="/custom-img/quochuy.png" :alt="`KHO DỮ LIỆU DÙNG CHUNG - ${currentOrg}`"
                            style="width: 2.8rem" />
                    </div>
                    <div class="ml-2">
                        <div class="mb-1">
                            <img src="/custom-img/logo-text.svg" style="height: 1.2rem;"/>
                            </div>
                            <div class="d-none d-md-block text-warning" style="font-size: 80%;">
                                {{ currentOrg }}
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="ml-2">
                        <div class="d-block d-sm-none">
                            <strong class="text-white">KHO DỮ LIỆU DÙNG CHUNG</strong>
                        </div>
                        <div class="d-block d-sm-none text-warning">
                            {{ currentOrg }}
                        </div>
                    </div>
                </li>
            </ul>

            <!-- Right navbar links -->
            <ul class="navbar-nav ml-auto"> 
                <li class="nav-item dropdown">
                    <a class="nav-link pt-2 pb-2" data-toggle="dropdown" href="#" style="height: auto;">
                        <el-tooltip content="Danh sách ứng dụng">
                            <el-icon color="#fff" size="32">
                                <Menu />
                            </el-icon>
                        </el-tooltip>
                    </a>
                    <div class="dropdown-menu dropdown-menu-xl dropdown-menu-right p-3" style="z-index: 99999;">
                        <a href="https://covid-tq.dtcsolution.vn" target="_blank" class="dropdown-item">
                            <div class="w-100 d-flex align-items-center">
                                <div class="text-center" style="width: 2em;">
                                    <img src="/apps-icon/medical.svg"
                                        style="width: 2.5rem;" />
                                </div>
                                <div class="ml-3">
                                    <div>
                                        <h6 class="m-0"><strong>Covid - Y tế</strong></h6>
                                    </div>
                                    <div style="font-size:90%;" class="text-muted"><i>Tra cứu thông tin Covid-19</i></div>
                                </div>
                            </div>
                        </a>
                        <a href="https://wiki-dx.dtcsolution.vn" target="_blank" class="dropdown-item">
                            <div class="w-100 d-flex align-items-center">
                                <div class="text-center" style="width: 2em;">
                                    <img src="/apps-icon/wiki.svg"
                                        style="width: 2.5rem;" />
                                </div>
                                <div class="ml-3">
                                    <div>
                                        <h6 class="m-0"><strong>Wiki-dx</strong></h6>
                                    </div>
                                    <div style="font-size:90%;" class="text-muted"><i>Tra cứu thủ tục hành chính</i></div>
                                </div>
                            </div>
                        </a>
                        <a href="https://wiki.dtcsolution.vn" target="_blank" class="dropdown-item">
                            <div class="w-100 d-flex align-items-center">
                                <div class="text-center" style="width: 2em;">
                                    <img src="/apps-icon/enterprise.svg"
                                        style="width: 2.5rem;" />
                                </div>
                                <div class="ml-3">
                                    <div>
                                        <h6 class="m-0"><strong>Wiki</strong></h6>
                                    </div>
                                    <div style="font-size:90%;" class="text-muted"><i>Tra cứu thông tin cá nhân, doanh
                                            nghiệp</i></div>
                                </div>
                            </div>
                        </a>
                        <a href="https://drive.dtcsolution.vn" target="_blank" class="dropdown-item">
                            <div class="w-100 d-flex align-items-center">
                                <div class="text-center" style="width: 2em;">
                                    <img src="/apps-icon/drive.svg"
                                        style="width: 2.5rem;" />
                                </div>
                                <div class="ml-3">
                                    <div>
                                        <h6 class="m-0"><strong>Drive</strong></h6>
                                    </div>
                                    <div style="font-size:90%;" class="text-muted"><i>Kho dữ liệu cá nhân</i></div>
                                </div>
                            </div>
                        </a>
                        <a href="https://ckan.dtcsolution.vn" target="_blank" class="dropdown-item">
                            <div class="w-100 d-flex align-items-center">
                                <div class="text-center" style="width: 2em;">
                                    <img src="/apps-icon/sharing.svg"
                                        style="width: 2.5rem;" />
                                </div>
                                <div class="ml-3">
                                    <div>
                                        <h6 class="m-0"><strong>CKAN</strong></h6>
                                    </div>
                                    <div style="font-size:90%;" class="text-muted"><i>Chia sẻ dữ liệu mở</i></div>
                                </div>
                            </div>
                        </a>
                    </div>
                </li>
                <!-- Notifications Dropdown Menu -->
                <li class="nav-item dropdown d-none d-md-block">
                    <a class="nav-link" data-toggle="dropdown" href="#" style="height: auto;">
                        <i class="far fa-bell text-white fa-2x"></i>
                        <span class="badge badge-warning navbar-badge">15</span>
                    </a>
                    <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right"
                        style="width: auto !important; max-width: none;">
                        <NotificationDropdown/>
                    </div>
                </li>
                <li class="nav-item ml-3">
                    <!-- Sidebar user (optional) -->
                    <AccountDropdown :is-hide-text="true" :currentUser="{ ...currentUser }" />
                </li>
            </ul>
        </nav>
        <!-- /.navbar -->
        <!-- Main Sidebar Container -->
        <aside class="main-sidebar main-sidebar-custom bg-transparent-color ">
            <!-- Sidebar -->
            <div class="d-flex align-item-center ml-0 pt-2 pb-2 pr-2 pl-2 logo-style">
                <div>
                    <img src="/custom-img/quochuy.png" alt="KHO DỮ LIỆU DÙNG CHUNG - TỈNH TUYÊN QUANG"
                        style="width: 2.8rem" />
                </div>
                <div class="ml-2 ml-md-2 text-left d-flex flex-column align-item-center justify-content-center"
                    style="line-height: 1.4rem">
                    <div class="d-block d-lg-none">
                        <strong class="text-white text-nowrap overflow-hidden">KHO DỮ LIỆU DÙNG CHUNG</strong>
                    </div>
                    <div class="d-block d-lg-none text-warning">
                        {{ currentOrg }}
                    </div>
                    <div class="ml-2 d-none d-lg-block">
                        <div>
                            <img src="/custom-img/logo-text.svg" style="width: 95%;"/>
                        </div>
                        <div class="d-none d-md-block text-warning" style="font-size: 80%;">
                            {{ currentOrg }}
                        </div>
                    </div>
                </div>
            </div>
            <div class="sidebar" style="height: calc(100% - 90px)">
                <!-- Sidebar Menu -->
                <nav class="mt-2">
                    <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu">
                        <!-- data-accordion="false" -->
                        <!-- Add icons to the links using the .nav-icon class with font-awesome or any other icon font library -->

                        <li v-bind:class="{
                                'nav-item': ['link', 'relative-link'].includes(navItem.type),
                                'nav-header': navItem.type === 'navHeader',
                                'menu-is-opening': navItem.toggleSubMenu,
                                'menu-open': navItem.toggleSubMenu,
                                'navItem-actived': navItem.toggleSubMenu && (navItem.childItems)
                            }" 
                            v-for="(navItem, navItemIndex) in navItemsRef" :key="navItemIndex">
                            <router-link v-if="navItem.type === 'link' && !(navItem.childItems)" :to="`${navItem.url}`"
                                class="nav-link pl-1" active-class="active">
                                <i :class="`nav-icon ${navItem.icon}`"></i>
                                <p>{{ navItem.name }}</p>
                            </router-link>
                            <a class="nav-link pl-1" :href="navItem.url"
                                v-if="navItem.type === 'relative-link' && !(navItem.childItems)" target="_blank">
                                <i :class="`nav-icon ${navItem.icon}`"></i>
                                <p>{{ navItem.name }}</p>
                            </a>
                            <span class="text-orange" v-if="navItem.type === 'navHeader' && !(navItem.childItems)">
                                {{ navItem.name }}
                            </span>
                            <a href="javascript:void(0);" class="nav-link nav-link-has-sub-items pl-1"
                                @click="toggleMenu(navItem)" 
                                    v-bind:class="{ 'active': navItem.childItems && navItem.childItems.map((xNavItem: any) => xNavItem.url).includes($route.path), }"
                                    v-if="navItem.type === 'link' && (navItem.childItems && navItem.childItems.length > 0)">
                                <i :class="`nav-icon  ${navItem.icon ? navItem.icon : 'fas fa-copy'}`"></i>
                                <p>
                                    {{ navItem.name }}
                                    <i class="fas fa-angle-left right"></i>
                                    <!-- <span class="badge badge-info right">6</span> -->
                                </p>
                            </a>
                            <ul class="nav nav-treeview" :style="`display: ${navItem.toggleSubMenu ? 'block' : 'none'}; `"
                                v-if="navItem.type === 'link' && (navItem.childItems && navItem.childItems.length > 0)">
                                <li class="nav-item" v-for="subItem in navItem.childItems" :key="subItem.name">
                                    <router-link v-if="subItem.type === 'link'"
                                        :to="subItem.url" 
                                        class="nav-link pl-3" 
                                        active-class="active">
                                        <p>• {{ subItem.name }}</p>
                                    </router-link>
                                    <a class="nav-link pl-3" :href="subItem.url"
                                        v-if="subItem.type === 'relative-link'" target="_blank"> 
                                        <p>• {{ subItem.name }}</p>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </nav>
                <!-- /.sidebar-menu -->
            </div>
            <!-- /.sidebar -->
        </aside>

        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper bg-transparent-color">
            <!-- Main content -->
            <section class="content">
                <div class="container-fluid p-0 pt-0 pb-2 d-flex flex-column w-100" 
                    style="height: calc(100vh - 4.0rem)">
                    <RouterView :key="route.path" />
                </div>
            </section>
            <!-- /.content -->
        </div>
        <!-- /.content-wrapper -->

        <!-- <footer class="main-footer">
            <div class="float-right d-none d-sm-block">
                <b>Version</b> 3.2.0
            </div>
            <strong>Copyright &copy; 2014-2021 <a href="https://adminlte.io">AdminLTE.io</a>.</strong> All rights
            reserved.
        </footer> -->

        <!-- Control Sidebar -->
        <aside class="control-sidebar control-sidebar-dark">
            <!-- Control sidebar content goes here -->
        </aside>
        <!-- /.control-sidebar -->
    </div>
</template>
  -->