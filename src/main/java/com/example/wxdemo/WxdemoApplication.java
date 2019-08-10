package com.example.wxdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
public class WxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxdemoApplication.class, args);
    }
}
