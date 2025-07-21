# traloitudong

chatbot-middleware/
├── src/
│   ├── api/
│   │   ├── controllers/
│   │   │   ├── authController.js
│   │   │   ├── customerController.js
│   │   │   └── chatbotController.js
│   │   ├── routes/
│   │   │   ├── authRoutes.js
│   │   │   ├── customerRoutes.js
│   │   │   └── chatbotRoutes.js
│   │   └── middlewares/
│   │       ├── authMiddleware.js
│   │       └── validationMiddleware.js
│   ├── services/
│   │   ├── chatwootService.js        # Tương tác với Chatwoot API
│   │   ├── botpressService.js        # Tương tác với Botpress API
│   │   ├── zaloService.js            # Tương tác với Zalo API
│   │   ├── facebookService.js        # Tương tác với Facebook API
│   │   └── customerService.js        # Logic nghiệp vụ liên quan đến khách hàng
│   ├── models/
│   │   ├── customerModel.js          # Định nghĩa schema/model cho khách hàng
│   │   ├── chatbotModel.js           # Định nghĩa schema/model cho chatbot
│   │   └── channelConfigModel.js     # Định nghĩa schema/model cho cấu hình kênh
│   ├── utils/
│   │   ├── errorHandler.js           # Xử lý lỗi tập trung
│   │   ├── constants.js              # Các hằng số
│   │   └── helpers.js                # Các hàm tiện ích chung
│   ├── config/
│   │   ├── database.js               # Cấu hình kết nối DB của middleware
│   │   ├── apiKeys.js                # Quản lý các API key (có thể dùng biến môi trường)
│   │   └── index.js                  # Cấu hình chung của ứng dụng
│   ├── webhooks/
│   │   ├── chatwootWebhookHandler.js # Xử lý webhook từ Chatwoot
│   │   ├── zaloWebhookHandler.js     # Xử lý webhook từ Zalo
│   │   └── facebookWebhookHandler.js # Xử lý webhook từ Facebook
│   ├── app.js                        # File chính của ứng dụng Express/NestJS
│   └── server.js                     # File khởi động server (nếu tách app.js)
├── public/                           # Nơi chứa các file tĩnh của Frontend (Vue.js build)
│   ├── index.html
│   ├── css/
│   ├── js/
│   └── img/
├── tests/
│   ├── unit/
│   └── integration/
├── .env                              # Biến môi trường (không commit lên Git)
├── .gitignore                        # Các file/thư mục cần bỏ qua bởi Git
├── package.json                      # Thông tin dự án và dependencies
├── package-lock.json                 # Hoặc yarn.lock
└── README.md                         # Hướng dẫn cài đặt và sử dụng
