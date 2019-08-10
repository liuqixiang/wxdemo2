package com.example.wxdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 文件上传相关属性
 * @create 
 **/
@Component
@ConfigurationProperties(prefix = "web")
public class FileUploadProperteis {

	
	private String upPath;//上传路径
	private String upUrl;//访问路径
	public String getUpPath() {
		return upPath;
	}
	public void setUpPath(String upPath) {
		this.upPath = upPath;
	}
	public String getUpUrl() {
		return upUrl;
	}
	public void setUpUrl(String upUrl) {
		this.upUrl = upUrl;
	}
	
	
	
}
