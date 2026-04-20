# 🚀 Real-Time Chat Application
Một ứng dụng nhắn tin thời gian thực hiệu suất cao được xây dựng trên hệ sinh thái Java Spring Boot, tận dụng sức mạnh của WebSocket và bộ nhớ đệm Redis để đảm bảo trải nghiệm mượt mà.

# 🎬 Demo

https://github.com/user-attachments/assets/7c1dbd18-1593-47af-a0de-814c0999f810

# ✨ Tính năng chính
- 💬 **Chat Real-time**: Gửi và nhận tin nhắn ngay lập tức.
- 👥 **Group Chat**: Tạo phòng chat nhóm không giới hạn thành viên.
- 🟢 **Online Status**: Theo dõi trạng thái hoạt động của bạn bè.
- 📜 **Chat History**: Lưu trữ lịch sử tin nhắn vào MySQL.
- ⚡ **High Performance**: Tối ưu hóa tốc độ truy xuất với Redis Cache.
# 🛠 Tech Stack
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

# ⚙️ Hướng dẫn cài đặt

## 1. Yêu cầu hệ thống
- JDK 21 trở lên
- MySQL 8.0+
- Redis 8.0+

## 2. Cấu hình Database
Tạo database trong MySQL:

```
CREATE DATABASE chat_app;
```

## 3. Cấu hình Application

Chỉnh sửa file src/main/resources/application.yml:

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chat_app
    username: your_user
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

## 4. Chạy ứng dụng

```
./mvnw spring-boot:run
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
src/main/java/com/project/chat
├── common          # Các kiểu dữ liệu 
├── configuration   # Cấu hình WebSocket, Redis, Security
├── constant        # Các biến constant
├── controller      # Xử lý Request & Message Mapping
├── dto             # DTO
├── entity          # Entity
├── exception       # Handing Error
├── mapper          # Convert to Response type
├── repository      # Giao tiếp DB (JPA)
├── service         # Logic nghiệp vụ
```

# 📝 License

Contact: Nguyễn Đình Huy Hoàng - [ndhhoang.02052002@gmail.com]




