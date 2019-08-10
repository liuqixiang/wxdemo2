package com.example.wxdemo.fillter;

import com.alibaba.fastjson.JSONObject;
import com.example.wxdemo.service.TokenService;
import com.example.wxdemo.utils.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lqx
 * @create 2018-08-07 9:29
 */
public class TokenFillter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TokenFillter.class);

    @Autowired
    private TokenService tokenService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        logger.info("Token 过滤器----配置初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpServletRequest req = (HttpServletRequest) request;

        String uri = req.getRequestURI();// 获取用户请求的地址
        if (uri != null && !uri.equals("")) {
            // 如果请求登陆地址，直接放行
            if (uri.indexOf("Login") > 0 ) {
                chain.doFilter(request, response);// 放行。让其走到下个链或目标资源中
            } else {
                String token = ((HttpServletRequest) request).getHeader("WX_TOKEN"); // 令牌

                if (StringUtils.isNotBlank(token)) {
                    try {
                        //验证token是否有效
                        boolean valid = tokenService.validateToken(token);// 验证用户传入的token是否有效

                        if (valid) {
                            chain.doFilter(request, response);// 放行。让其走到下个链或目标资源中
                        } else {
                            response.getWriter().print(JSONObject.toJSONString(ResultData.build(201, "传入了无效的token")));
                            response.getWriter().flush();
                            response.getWriter().close();
                            logger.warn("=================传入了无效的token=====================");
                        }
                    } catch (Exception e) {

                        response.getWriter().print(JSONObject.toJSONString(ResultData.build(201, "传入了无效的token")));
                        response.getWriter().flush();
                        response.getWriter().close();
                        logger.error("=================token无效=====================");
                        logger.error(e.getMessage());
                    }
                } else {
                    response.getWriter().print(JSONObject.toJSONString(ResultData.build(203, "token是空的")));
                    response.getWriter().flush();
                    response.getWriter().close();
                    logger.warn("=================token为空=====================");
                }
            }
        }



    }

    @Override
    public void destroy() {
        logger.info("Token 过滤器----已销毁");
    }
}
