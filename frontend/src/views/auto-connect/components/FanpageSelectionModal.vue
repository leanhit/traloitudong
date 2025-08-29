<script
    lang="ts"
    src="@/scripts/auto-connect/components/FanpageSelectionModal.ts"></script>

<template>
    <div class="modal-overlay" @click.self="$emit('close')">
        <div class="modal-container" v-loading="isLoading">
            <div class="modal-header">
                <h2 class="modal-title">Chọn Fanpage để kết nối</h2>
                <button class="close-btn" @click="$emit('close')">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 24 24"
                        fill="currentColor"
                        class="w-6 h-6"
                    >
                        <path
                            fill-rule="evenodd"
                            d="M5.47 5.47a.75.75 0 011.06 0L12 10.94l5.47-5.47a.75.75 0 111.06 1.06L13.06 12l5.47 5.47a.75.75 0 11-1.06 1.06L12 13.06l-5.47 5.47a.75.75 0 01-1.06-1.06L10.94 12 5.47 6.53a.75.75 0 010-1.06z"
                            clip-rule="evenodd"
                        />
                    </svg>
                </button>
            </div>

            <div class="modal-body">
                <p class="modal-description">
                    Dưới đây là danh sách các Fanpage bạn đã cấp quyền. Vui lòng chọn một trang hoặc kết nối tất cả.
                </p>

                <div class="bot-selection-wrapper">
                    <label for="bot-select" class="bot-label">Chọn Bot ID:</label>
                    <el-select 
                        v-model="selectedBotId" 
                        placeholder="Chọn một Bot"
                        class="bot-select"
                    >
                        <el-option
                            v-for="item in botIdOptions"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value"
                        />
                    </el-select>
                </div>

                <div v-if="pages && pages.length > 0" class="page-grid">
                    <div v-for="page in pages" :key="page.pageId" class="page-card">
                        <div class="card-header">
                            <img
                                :src="page.thumbnail"
                                alt="Page Thumbnail"
                                class="page-thumbnail"
                            />
                            <div class="page-info">
                                <h3 class="page-name">{{ page.pageName }}</h3>
                                <span class="page-id">ID: {{ page.pageId }}</span>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="info-row">
                                <strong>Token:</strong>
                                <span class="token-text">
                                    {{ truncateToken(page.pageAccessToken) }}
                                </span>
                                <button @click="copyToken(page.pageAccessToken)" class="copy-btn">
                                    Sao chép
                                </button>
                            </div>
                            <div class="info-row">
                                <strong>URL:</strong>
                                <a
                                    :href="getPageUrl(page.pageId)"
                                    target="_blank"
                                    class="page-url"
                                >
                                    Truy cập trang
                                </a>
                            </div>
                        </div>
                        <div class="card-footer">
                            <button @click="connectPage(page)" class="connect-button">
                                Kết nối
                            </button>
                        </div>
                    </div>
                </div>

                <div v-else class="no-pages-message">
                    <p>Không tìm thấy fanpage nào. Vui lòng đảm bảo bạn đã cấp quyền.</p>
                </div>
            </div>

            <div v-if="pages && pages.length > 0" class="modal-footer">
                <button @click="connectAllPages" class="connect-all-button">
                    Kết nối tất cả Fanpage
                </button>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Modal Overlay và Container */
.modal-overlay {
 position: fixed;
 top: 0;
 left: 0;
 width: 100%;
 height: 100%;
 background-color: rgba(0, 0, 0, 0.6);
 display: flex;
 justify-content: center;
 align-items: center;
 z-index: 1000;
 backdrop-filter: blur(5px);
}
.modal-container {
 background: #ffffff;
 border-radius: 12px;
 box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
 width: 95%;
 max-width: 900px;
 overflow: hidden;
 display: flex;
 flex-direction: column;
 animation: fadeIn 0.3s ease-out;
}

/* Modal Header */
.modal-header {
 display: flex;
 justify-content: space-between;
 align-items: center;
 padding: 16px 20px;
 border-bottom: 1px solid #e0e0e0;
 background-color: #f7f7f7;
}

.modal-title {
 font-size: 20px;
 font-weight: 700;
 color: #333;
 margin: 0;
}

.close-btn {
 background: none;
 border: none;
 cursor: pointer;
 padding: 5px;
 border-radius: 50%;
 transition: background-color 0.2s;
}
.close-btn:hover {
 background-color: #e0e0e0;
}
.close-btn svg {
 width: 24px;
 height: 24px;
 color: #666;
}

/* Modal Body */
.modal-body {
 padding: 20px;
 max-height: 70vh;
 overflow-y: auto;
}

.modal-description {
 font-size: 15px;
 color: #555;
 margin-bottom: 20px;
 line-height: 1.5;
}

/* Page Grid (New Layout) */
.page-grid {
 display: grid;
 grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
 gap: 20px;
}

.page-card {
 background: #f9f9f9;
 border: 1px solid #e0e0e0;
 border-radius: 8px;
 overflow: hidden;
 display: flex;
 flex-direction: column;
 transition: box-shadow 0.2s;
}

.page-card:hover {
 box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
 display: flex;
 align-items: center;
 padding: 15px;
 border-bottom: 1px solid #e0e0e0;
}

.page-thumbnail {
 width: 50px;
 height: 50px;
 border-radius: 50%;
 margin-right: 15px;
 object-fit: cover;
}

.page-info {
 flex-grow: 1;
}

.page-name {
 font-size: 16px;
 font-weight: 600;
 margin: 0 0 4px 0;
 color: #333;
}

.page-id {
 font-size: 12px;
 color: #777;
}

.card-body {
 padding: 15px;
 display: flex;
 flex-direction: column;
 gap: 10px;
}

.info-row {
 display: flex;
 align-items: center;
 font-size: 14px;
 color: #555;
}

.token-text {
 background-color: #e9ebee;
 padding: 4px 8px;
 border-radius: 4px;
 font-family: monospace;
 margin-right: 10px;
}

.copy-btn {
 background: #f0f2f5;
 border: 1px solid #ccd0d5;
 color: #4267b2;
 font-size: 12px;
 padding: 4px 8px;
 border-radius: 4px;
 cursor: pointer;
 transition: background-color 0.2s;
}
.copy-btn:hover {
 background-color: #e4e6eb;
}

.page-url {
 color: #4267b2;
 text-decoration: none;
 font-weight: 500;
}
.page-url:hover {
 text-decoration: underline;
}

.card-footer {
 padding: 15px;
 border-top: 1px solid #e0e0e0;
 text-align: right;
}

/* Buttons */
.connect-button {
 padding: 8px 18px;
 background-color: #4267b2;
 color: white;
 border: none;
 border-radius: 6px;
 font-size: 14px;
 font-weight: 600;
 cursor: pointer;
 transition: background-color 0.2s ease;
}

.connect-button:hover {
 background-color: #365899;
}

.modal-footer {
 padding: 15px 20px;
 border-top: 1px solid #e0e0e0;
 background-color: #f7f7f7;
 text-align: center;
}

.connect-all-button {
 padding: 12px 24px;
 background-color: #42b72a;
 color: white;
 border: none;
 border-radius: 6px;
 font-size: 16px;
 font-weight: 600;
 cursor: pointer;
 transition: background-color 0.2s ease;
}

.connect-all-button:hover {
 background-color: #318a20;
}

.bot-selection-wrapper {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.bot-label {
  font-weight: bold;
  color: #555;
}

.bot-select {
  flex-grow: 1;
  max-width: 300px;
}
</style>