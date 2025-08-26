<template>
  <button @click="loginWithFacebook" class="facebook-button">
    <img
      src="https://static.xx.fbcdn.net/rsrc.php/v3/yS/r/C_5f8W3w1n-.png"
      alt="Facebook logo"
      class="icon"
    />
    Đăng nhập với Facebook
  </button>
</template>

<script>
export default {
  name: "FacebookLogin",
  emits: ["loginSuccess", "loginFailure"],
  methods: {
    loginWithFacebook() {
      if (typeof window.FB === "undefined") {
        console.error("❌ Facebook SDK chưa load!");
        return;
      }

      window.FB.login(
        (response) => {
          if (response.authResponse) {
            const { accessToken, userID } = response.authResponse;
            console.log("✅ Đăng nhập thành công!", accessToken, userID);

            // Emit lên component cha
            this.$emit("loginSuccess", { accessToken, userID });
          } else {
            console.log("⚠️ Người dùng hủy login hoặc không cấp quyền");
            this.$emit("loginFailure", "Đăng nhập bị hủy.");
          }
        },
        { scope: "public_profile,email" }
      );
    },
  },
};
</script>

<style scoped>
.facebook-button {
  display: inline-flex;
  align-items: center;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #4267b2;
  color: white;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
}
.facebook-button:hover {
  background-color: #365899;
}
.icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}
</style>
