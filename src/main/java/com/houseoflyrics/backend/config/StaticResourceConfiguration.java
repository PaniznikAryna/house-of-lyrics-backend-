package com.houseoflyrics.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Отдаем файлы из папки uploads/photos по URL /uploads/photos/**
        registry.addResourceHandler("/uploads/photos/**")
                .addResourceLocations("file:uploads/photos/");
        // Отдаем файлы из папки uploads/music по URL /uploads/music/**
        registry.addResourceHandler("/uploads/music/**")
                .addResourceLocations("file:uploads/music/");
    }
}
