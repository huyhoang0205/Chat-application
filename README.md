# 🚀 ChatApp - Frontend
Ứng dụng chat real-time được xây dựng với Next.js, Material-UI và TypeScript.

# ✨ Tính năng nổi bật
**Authentication**
- Đăng ký tài khoản mới
- Đăng nhập với email/password
- Auto-redirect sau đăng ký
- Token management (localStorage)
- Auto-refresh token (401 handling)

**Chat**
- Danh sách conversations
- Tìm kiếm user để tạo conversation
- Gửi/nhận tin nhắn text
- Phân biệt tin nhắn của mình và người khác
- Hiển thị avatar, tên người gửi
- Format thời gian tin nhắn
- Optimistic update với tempId

**UI/UX**
- Dark theme (Discord-inspired)
- Responsive design
- Loading states
- Empty states với animation
- Hover effects
- Auto-scroll messages
- Sidebar toggle (mobile)

# 🛠 Tech Stack
- Framework: Next.js 16.1.6 (App Router)
- Language: TypeScript
- UI Library: Material-UI (MUI) v6
- State Management: Zustand
- HTTP Client: Axios
- Date Formatting: date-fns
- Styling: MUI System + CSS-in-JS

# ⚙️ Cài đặt và Chạy thử


## 1. Clone dự án

```
git clone https://github.com/huyhoang0205/Chat-application/tree/frontend
cd frontend
```

## 2. Yêu cầu:
- Node.js 18+
- npm hoặc yarn
## 3. Cài đặt thư viện và khởi chạy

```
# Install dependencies
npm install

# Run development server
npm run dev

```

# 🐳 Triển khai với Docker
Dự án đã được cấu hình Docker Compose để bạn có thể khởi chạy toàn bộ môi trường (Node.js, MongoDB, Redis) một cách nhanh chóng.

### Yêu cầu:
* Đã cài đặt [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Các bước thực hiện:
1. Tạo file `.env` (như hướng dẫn ở trên).
2. Chạy lệnh sau tại thư mục gốc:

   ```bash
   docker-compose up --build
   ```
# 📂 Cấu trúc thư mục

```
chat-app-web/
├── app/                  # Next.js App Router
├── components/           # React components
│   ├── auth/             # Components liên quan authentication
│   ├── chat/             # Components chat
│   └── common/           # Components dùng chung
├── hooks/                # Custom React hooks
├── services/             # API services
├── store/                # Zustand stores
├── types/                 # TypeScript types
├── api/                   # API configuration
└── public/               # Static files
    └── ...
```

# 📝 License

Contact: Nguyễn Đình Huy Hoàng - [ndhhoang.02052002@gmail.com]
