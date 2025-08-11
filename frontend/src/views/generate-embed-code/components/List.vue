<script
    lang="ts"
    src="@/scripts/generate-embed-code/components/list.ts"></script>

<template>
    <div class="flex-fill d-flex flex-column w-100 p-2">
        <!-- header -->
        <div class="d-flex align-items-center justify-content-between">
            <div class="page-titles">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item active">
                        <a href="javascript:void(0)">{{
                            viewSettings.title
                        }}</a>
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
                        <div class="">
                            <h2>Tạo & Test Mã Nhúng Botpress Webchat</h2>
                            <p>
                                Vui lòng nhập ID Bot của bạn. Sau đó, chọn "Tạo
                                Mã Nhúng" hoặc "Kiểm Tra Bot".
                            </p>

                            <label for="botIdInput">Nhập Bot ID của bạn:</label>
                            <input
                                type="text"
                                id="botIdInput"
                                v-model="botId"
                                placeholder="Ví dụ: myawesomebot123" />

                            <div class="button-group">
                                <button
                                    :class="{
                                        active: activeSection === 'code',
                                    }"
                                    @click="showSection('code')">
                                    Tạo Mã Nhúng
                                </button>
                                <button
                                    :class="{
                                        active: activeSection === 'test',
                                    }"
                                    @click="showSection('test')">
                                    Kiểm Tra Bot
                                </button>
                            </div>

                            <div
                                id="codeSection"
                                class="section"
                                :class="{ hidden: activeSection !== 'code' }">
                                <div id="embedCodeResult" class="result-box">
                                    <div class="copy-controls">
                                        <button @click="copyEmbedCode">
                                            📋 Sao chép mã nhúng
                                        </button>
                                        <span
                                            v-show="copyStatusVisible"
                                            id="copyStatus"
                                            >Đã sao chép!</span
                                        >
                                    </div>

                                    <p>
                                        Mã nhúng của bạn (có thể sao chép để
                                        dùng trên trang web chính):
                                    </p>
                                    <code id="generatedCode">{{
                                        generatedCode
                                    }}</code>

                                    <p
                                        style="
                                            margin-top: 15px;
                                            font-size: 0.9em;
                                            color: #555;
                                        ">
                                        Dán đoạn mã này vào cuối thẻ
                                        `&lt;body&gt;` của trang HTML mục tiêu
                                        của bạn.
                                    </p>
                                </div>
                            </div>

                            <div
                                id="testSection"
                                class="section"
                                :class="{ hidden: activeSection !== 'test' }">
                                <div id="testFrameContainer">
                                    <h3>Khung Test Botpress Webchat</h3>
                                    <p>
                                        Bot của bạn sẽ xuất hiện trong khung bên
                                        dưới. Nếu không thấy, hãy kiểm tra Bot
                                        ID, URL server và console trình duyệt để
                                        tìm lỗi (ví dụ: lỗi CORS).
                                    </p>
                                    <iframe
                                        id="testIframe"
                                        ref="testIframeRef"
                                        title="Botpress Webchat Test Frame"></iframe>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Base styles */
body {
    /* This body style won't apply directly in a Vue component */
    font-family: Arial, sans-serif;
    margin: 20px;
    background-color: #f4f4f4;
    color: #333;
}

.container {
    background-color: #fff;
    padding: 25px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    max-width: 800px;
    margin: 30px auto;
}

h2 {
    color: #0056b3;
    text-align: center;
    margin-bottom: 25px;
}

label {
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
}

input[type='text'] {
    width: calc(100% - 22px);
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 16px;
}

.button-group {
    display: flex;
    gap: 10px;
    margin-bottom: 15px;
}

.button-group button {
    flex-grow: 1;
    background-color: #007bff;
    color: white;
    padding: 12px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease;
}

.button-group button:hover {
    background-color: #0056b3;
}

.button-group button.active {
    background-color: #0056b3;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
}

.result-box {
    background-color: #e9e9e9;
    padding: 15px;
    border-radius: 5px;
    margin-top: 25px;
    word-wrap: break-word;
    white-space: pre-wrap;
    font-family: 'Courier New', Courier, monospace;
    color: #000;
    border: 1px dashed #ccc;
    position: relative;
}

.hidden {
    display: none !important; /* Use !important to override other display properties */
}

#testFrameContainer {
    margin-top: 30px;
    border-top: 2px solid #007bff;
    padding-top: 20px;
}

#testIframe {
    width: 100%;
    height: 400px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f0f0f0;
}

.warning {
    color: #d9534f;
    background-color: #fcebeb;
    border: 1px solid #d9534f;
    padding: 10px;
    border-radius: 4px;
    margin-bottom: 20px;
}

.copy-controls {
    position: absolute;
    top: 10px;
    right: 10px;
    margin-top: 0;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
}

.copy-controls button {
    padding: 6px 12px;
    font-size: 14px;
    cursor: pointer;
    border: none;
    border-radius: 4px;
    background-color: #28a745;
    color: #fff;
    margin-bottom: 5px;
}

.copy-controls span {
    display: block;
    color: green;
    font-weight: bold;
    font-size: 13px;
    white-space: nowrap;
}
</style>
