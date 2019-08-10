package com.example.wxdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * springboot配置虚拟目录
 * @author lqx
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
	
	 @Autowired
	 private FileUploadProperteis fileUploadProperteis;
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileUploadProperteis.getUpUrl()).addResourceLocations("file:"+fileUploadProperteis.getUpPath());       
    }
}
