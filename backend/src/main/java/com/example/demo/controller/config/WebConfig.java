package com.example.demo.controller.config; // <-- đổi theo tên project của bạn
// C:\Users\2230110\Desktop\taphoathuavan\backend\src\main\java\com\example\demo\controller\config\WebConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Cho phép các đường dẫn bắt đầu bằng /api/
                        .allowedOrigins("http://localhost:3000") // Cho phép frontend React
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Cho phép gửi cookie (nếu cần)
            }
        };
    }
}
