<template>
    <div class="homepage-container">
        <h1>🎉 Hệ thống Chatbot đã khởi động!</h1>
        <p>Chào mừng bạn đến với middleware backend.</p>

        <div class="links">
            <a href="https://cwsv.traloitudong.com" target="_blank"
                >🗄️ Truy cập Chatwoot SV</a
            >
            <a href="https://chat.traloitudong.com" target="_blank"
                >💬 Truy cập Chatwoot UI</a
            >
            <a href="https://bot.traloitudong.com" target="_blank"
                >🤖 Truy cập Botpress</a
            >
        </div>
    </div>
</template>

<script setup>
// Không có logic Vue cụ thể nào cần thiết cho trang này
// vì nó chỉ hiển thị nội dung tĩnh và các liên kết.
// Nếu sau này bạn cần dữ liệu động hoặc tương tác,
// bạn sẽ thêm logic vào đây.
</script>

<style scoped>
/* Styles riêng cho component này */
.homepage-container {
    font-family: Arial, sans-serif;
    padding: 40px;
    background: #121212; /* Nền đen */
    color: #ffffff; /* Chữ trắng */
    text-align: center;
    min-height: 100vh; /* Đảm bảo nền đen bao phủ toàn bộ chiều cao */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

h1 {
    color: #ffffff;
    margin-bottom: 15px;
}

p {
    color: #dddddd;
    margin-bottom: 30px;
}

.links {
    margin-top: 30px;
    display: flex; /* Dùng flexbox để quản lý các nút */
    flex-wrap: wrap; /* Cho phép các nút xuống dòng trên màn hình nhỏ */
    justify-content: center; /* Căn giữa các nút */
    gap: 20px; /* Khoảng cách giữa các nút */
}

.links a {
    display: inline-block;
    padding: 12px 20px;
    text-decoration: none;
    color: white;
    background-color: #1e88e5;
    border-radius: 6px;
    transition: background 0.3s ease; /* Thêm hiệu ứng transition mượt mà hơn */
    font-weight: bold; /* Làm cho chữ trên nút nổi bật hơn */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2); /* Thêm đổ bóng nhẹ */
}

.links a:hover {
    background-color: #1565c0;
    transform: translateY(-2px); /* Hiệu ứng nhấc nhẹ khi hover */
    box-shadow: 0 6px 8px rgba(0, 0, 0, 0.3);
}
</style>
