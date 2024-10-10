package com.snsapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableSpringDataWebSupport
@PropertySource("classpath:upload_file.properties") // Đọc từ file properties
@ComponentScan(basePackages = "com.snsapi") // Quét các thành phần trong gói com.snsapi
@EnableJpaRepositories(basePackages = "com.snsapi") // Kích hoạt Spring Data JPA
public class ApplicationConfig implements WebMvcConfigurer {

    @Value("${upload.image}") // Đọc giá trị từ upload_file.properties
    private String fileUpload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để phục vụ hình ảnh từ đường dẫn tải lên
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + fileUpload);
    }
}
