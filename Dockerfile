FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Sao chép các file cấu hình phụ thuộc trước để tận dụng Docker Layer Caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw

# Tải trước các phụ thuộc (Dependencies)
RUN ./mvnw dependency:go-offline -B

# Sao chép toàn bộ mã nguồn còn lại
COPY src ./src

# Đóng gói ứng dụng (bỏ qua bước test để build nhanh hơn nếu cần)
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

# Tạo user riêng để chạy ứng dụng (Best Practice về bảo mật: Không chạy bằng root)
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Sao chép file JAR đã build từ giai đoạn 1 sang giai đoạn chạy
COPY --from=builder /app/target/*.jar app.jar

# Khai báo cổng mà ứng dụng lắng nghe
EXPOSE 8080

# Chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar", "--server.port=${PORT}"]