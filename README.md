# Giới thiệu
Chat application là một trong những ứng dụng phổ biến nhất hiện nay, từ các ứng dụng nhắn tin cá nhân (Messenger, WhatsApp, Telegram) đến các hệ thống chat trong doanh nghiệp (Slack, Microsoft Teams). Đây là 1 dự án xây dựng một ứng dụng chat real-time hoàn chỉnh với các tính năng:
- Chat 1-1 và nhóm
- Nhắn tin real-time với WebSocket
- Trạng thái online/offline
- Lịch sử tin nhắn và phân trang
- Chia sẻ file
- Xác thực và phân quyền người dùng
# Tech Stack

## Backend
**Spring Boot 4.x:**

- REST API cho quản lý người dùng và phòng chat
- WebSocket với giao thức STOMP
- Spring Security với xác thực JWT
- Spring Data JPA với MySQL

**MySQL:**

- Lưu trữ người dùng, phòng chat, tin nhắn
- Quan hệ giữa người dùng và phòng chat
- Lịch sử tin nhắn và metadata

**Redis:**

- Quản lý session cho WebSocket connections
- Theo dõi người dùng online
- Message queue cho gửi tin nhắn real-time
- Cache cho dữ liệu truy cập thường xuyên

**WebSocket + STOMP:**

- Giao tiếp hai chiều real-time
- Mô hình Pub/Sub cho chat nhóm
- Broadcasting tin nhắn
- Quản lý vòng đời kết nối

