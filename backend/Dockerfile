# Sử dụng image có sẵn OpenJDK 17
FROM openjdk:17

# Tạo thư mục làm việc trong container
WORKDIR /app

# Copy file .jar vào container
COPY target/*.jar app.jar

# Mở cổng 8080 (nơi Spring Boot chạy)
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
