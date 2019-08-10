package com.example.wxdemo.config;

import com.example.wxdemo.fillter.Token02Fillter;
import com.example.wxdemo.fillter.TokenFillter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * @author lqx
 * @create 2018-08-07 10:01
 */
@Configuration
public class FillterConfig {

    @Bean(name = "tokenFilter")
    public TokenFillter tokenFilter() {
        TokenFillter bean=new TokenFillter();
        return bean;
    }
    @Bean(name = "token02Filter")
    public Token02Fillter token02Filter() {
        Token02Fillter bean=new Token02Fillter();
        return bean;
    }
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        //TokenFillter filter = new TokenFillter();
        bean.setFilter(new DelegatingFilterProxy("tokenFilter"));
        bean.addUrlPatterns("/wechatApi/*");
        bean.setName("tokenFilter");
        bean.setOrder(1);
        return bean;
    }
    @Bean
    public FilterRegistrationBean filter02RegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new DelegatingFilterProxy("token02Filter"));
        bean.addUrlPatterns("/wechatApi02/*");
        bean.setName("token02Filter");
        bean.setOrder(2);
        return bean;
    }

}
