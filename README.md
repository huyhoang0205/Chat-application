# 🚀 E-commerce Backend API
Một hệ thống quản lý cửa hàng (E-commerce) được xây dựng bằng Node.js, Express và MongoDB.  

# ✨ Tính năng nổi bật
**Access Service**
- SignUp
- JWT 
- Login and Authentication
- Handle RefreshToken

**Product Service**
- Query Products [public]
- Query Products Using It's Id [Public]
- Create New Product [Shop]
- Update Product [Shop]
- Unpulish/Publish Product [Shop]
- Search Product [Public]
- Darf product [Shop]

**Discount Service**
- Generator Discount Code [Shop|Admin]
- Get All Discount Code [User|Shop]
- Get All Product By Discount Code [User]
- Get Discount Amount [User]
- Delete Discount Code [Admin|Shop]
- Cancel Discount Code [User] 

**Cart Service**
- Add Product To Cart [User]
- Reduce Product Quantity [User]
- Increase Product Quantity [User]
- Get List To Cart [User]
- Delete Cart [User]
- Delete Cart Item [User]

**Order Service**
- Create New Order [User]
- Query Order [User]
- Query Order Using It's ID [User]
- Update Order Status[Admin]

**Inventory Service**
- Add Inventory When Create Product [Shop]

**Comment Service(Nesting Comments)**
- Create Comment[Shop|User]
- Delete Comment[Shop|User]
- Get Comment[Shop|User]

**User Service**
- User Registration With Verify Email[User]

**Other**
- Handle Error / Handle Response
- Handle Routeing
- Connect MongoDB and tracking connection 

# 🛠 Tech Stack
- ![NodeJS](https://img.shields.io/badge/node.js-22+-6DA55F?style=for-the-badge&logo=node.js&logoColor=white)
- ![Express.js](https://img.shields.io/badge/express.js-%23404d59.svg?style=for-the-badge&logo=express&logoColor=%2361DAFB)
- ![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
- ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
- ![Swagger](https://img.shields.io/badge/-Swagger-%2385EA2D?style=for-the-badge&logo=swagger&logoColor=black)
- ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

# ⚙️ Cài đặt và Chạy thử


## 1. Clone dự án

```
git clone https://github.com/huyhoang0205/Ecommerce_Express
cd Ecommerce_Express
```

## 2. Cấu hình môi trường
Tạo file .env tại thư mục gốc và copy nội dung từ .env.example:

```
NODE_ENV=dev

DEV_DB_NAME=your_name_app
DEV_DB_HOST=your_mongodb_connection_host
DEV_DB_PORT=your_mongodb_connection_port
DEV_APP_PORT=your_app_port

EMAIL_USER=your_email_using_nodemail
EMAIL_PASS=google_app_password

REDIS_HOST=your_redis_connection_host
REDIS_PORT=your_redis_connection_port
```

## 3. Cài đặt thư viện và khởi chạy

```
# Cài đặt dependencies
npm install

# Chạy ở chế độ phát triển 
npm run start
or node --watch server.js

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
src/
 ├── auths/          # Auth
 ├── config/         # Cấu hình DB, Redis, NodeMail
 ├── constant/       # Cấu hình DB, Redis, NodeMail
 ├── controllers/    # Xử lý Request và gửi Response
 ├── cores/          # Responese code và Error code
 ├── dbs/            # Kết nối DB, Redis, NodeMail
 ├── helpers/        # Helper functions 
 ├── middlewares/    # Xử lý Role based control access
 ├── models/         # Mongoose Schemas (User, Product, Order)
 ├── public/         # HTML Email Xác thực, Email cung cấp mật khẩu
 ├── routes/         # Định nghĩa các API endpoints
 ├── services/       # Logic nghiệp vụ (Gửi mail, thanh toán)
 └── utils/          # Helper functions 
```

# 📖 Tài liệu API

Sau khi khởi chạy ứng dụng, bạn có thể truy cập tài liệu API đầy đủ (Swagger) tại:

URL: ```http://${host}$:${port}/api-docs```

# 📝 License

Contact: Nguyễn Đình Huy Hoàng - [ndhhoang.02052002@gmail.com]
