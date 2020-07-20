package com.example.demo.Dto;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //String path = ""file:C:/FotosProyecto"";
        //String path = "/home/ec2-user/FotosProyecto/";
        registry.addResourceHandler("/FotosProyecto/**")
                .addResourceLocations("file:C:/FotosProyecto");
    }
}
