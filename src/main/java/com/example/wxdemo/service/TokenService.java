package com.example.wxdemo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lqx
 * @create 2018-08-07 9:47
 */
@Service
public class TokenService {
    @Value("${wechat.app_timeout}")
    private Integer appTimeOut;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean validateToken(String wxToken){
        Object obj=redisTemplate.opsForValue().get(wxToken);
        if(obj!=null){
            redisTemplate.expire(wxToken,appTimeOut,TimeUnit.SECONDS);
            return true;
        }
        return false;
    }




}
