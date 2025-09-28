<template>
  <nav class="navbar-custom">
    <ul class="list-inline float-right mb-0">
      <!-- Search box -->
      <li class="list-inline-item hide-phone app-search">
        <SearchBox />
      </li>

      <!-- Language switch -->
      <li class="list-inline-item dropdown notification-list hide-phone">
        <a
          class="nav-link dropdown-toggle arrow-none waves-effect text-white"
          data-toggle="dropdown"
          href="#"
          role="button"
          aria-haspopup="false"
          aria-expanded="false"
        >
          <img
            :src="currentFlag"
            class="ml-2"
            height="16"
            alt="Language Flag"
          />
        </a>

        <div class="dropdown-menu dropdown-menu-right language-switch">
          <a
            class="dropdown-item"
            href="#"
            v-for="localeItem in supportedLocales"
            :key="localeItem.value"
            @click="changeLocale(localeItem.value)"
          >
            <img :src="localeItem.flag" alt="" height="16" />
            <span>{{ localeItem.label }}</span>
          </a>
        </div>
      </li>

      <!-- Profile dropdown -->
      <li class="list-inline-item dropdown notification-list">
        <a
          class="nav-link dropdown-toggle arrow-none waves-effect nav-user"
          data-toggle="dropdown"
          href="#"
          role="button"
          aria-haspopup="false"
          aria-expanded="false"
        >
          <img
            src="/images/users/avatar-1.jpg"
            alt="user"
            class="rounded-circle"
          />
        </a>

        <div
          class="dropdown-menu dropdown-menu-right profile-dropdown custom-profile-dropdown"
        >
          <template v-if="isAuthenticated">
            <div class="dropdown-item noti-title">
              <h5>{{ user?.email || "User" }}</h5>
            </div>
            <router-link to="/profile" class="dropdown-item">
              <i class="mdi mdi-account-circle m-r-5 text-muted"></i>
              {{ t("Profile") }}
            </router-link>
            <router-link to="/help" class="dropdown-item">
              <i class="mdi mdi-help-circle m-r-5 text-muted"></i>
              {{ t("Help") }}
            </router-link>

            <div class="dropdown-divider"></div>
            <a
              class="dropdown-item"
              href="#"
              @click.prevent="handleLogout"
            >
              <i class="mdi mdi-logout m-r-5 text-muted"></i>
              Logout
            </a>

            <div class="dropdown-divider"></div>

            <!-- 🔹 Dark/Light mode toggle -->
            <a
              class="dropdown-item"
              href="#"
              @click.prevent="toggleTheme"
            >
              <i
                class="mdi m-r-5 text-muted"
                :class="isDark ? 'mdi-white-balance-sunny' : 'mdi-weather-night'"
              ></i>
              {{ isDark ? "Light Mode" : "Dark Mode" }}
            </a>
          </template>
        </div>
      </li>
    </ul>

    <!-- Left menu -->
    <ul class="list-inline menu-left mb-0">
      <li class="float-left">
        <button
          class="button-menu-mobile open-left waves-light waves-effect"
        >
          <i class="mdi mdi-menu"></i>
        </button>
      </li>
    </ul>

    <div class="clearfix"></div>
  </nav>
</template>

<script lang="ts">
import { ref, onMounted, computed } from "vue";
import { useI18n } from "vue-i18n";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import { supportedLocales, currentLanguage } from "@/until/constant";
import { useAuthStore } from "@/stores/auth";
import SearchBox from "./SearchBox.vue";

export default {
  components: {
    SearchBox,
  },
  setup() {
    const { t, locale } = useI18n();
    const router = useRouter();

    // Auth store
    const authStore = useAuthStore();
    const { token, user } = storeToRefs(authStore);
    const isAuthenticated = computed(() => !!token.value);

    // Locale
    const currentLocale = ref(
      localStorage.getItem("restaurentLocale") || currentLanguage
    );
    if (currentLocale.value === null) {
      currentLocale.value = "en";
    }

    const currentFlag = ref("");
    const setFlag = () => {
      const found = supportedLocales.find(
        (lo) => lo.value === currentLocale.value
      );
      currentFlag.value = found ? found.flag : supportedLocales[0].flag;
    };
    setFlag();

    const changeLocale = (newLocale: string) => {
      localStorage.setItem("restaurentLocale", newLocale);
      currentLocale.value = newLocale;
      locale.value = newLocale;
      setFlag();
      location.reload();
    };

    // Logout
    function handleLogout() {
      authStore.logout();
      router.push("/login");
    }

    // 🔹 Dark/Light mode
    const isDark = ref(false);

    onMounted(() => {
      // Check trạng thái dark từ localStorage hoặc hệ điều hành
      isDark.value =
        localStorage.getItem("theme") === "dark" ||
        window.matchMedia("(prefers-color-scheme: dark)").matches;

      if (isDark.value) {
        document.body.classList.add("dark");
      }

      // Script Bootstrap/JQuery dropdown
      const recaptchaScript = document.createElement("script");
      recaptchaScript.setAttribute("src", "/js/app.js");
      document.head.appendChild(recaptchaScript);
    });

    function toggleTheme() {
      isDark.value = !isDark.value;
      document.body.classList.toggle("dark", isDark.value);
      localStorage.setItem("theme", isDark.value ? "dark" : "light");
    }

    return {
      t,
      isAuthenticated,
      handleLogout,
      changeLocale,
      currentLocale,
      currentFlag,
      supportedLocales,
      user,
      isDark,
      toggleTheme,
    };
  },
};
</script>

<style scoped>
/* Profile dropdown rộng hơn */
.profile-dropdown.custom-profile-dropdown {
  min-width: 270px;
}
</style>
