package org.springbus.moban.web.filiter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springbus.moban.web.entity.UserEntity;
import org.springbus.moban.emus.ResponseCode;
import org.springbus.moban.web.LoginUserCache;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Slf4j
@Order(1)
@Component
public class LoginFilter implements Filter {

    //定义了一组不需要经过登录验证的 URL 路径，包括登录、注册、登出页面以及静态资源文件（如图片、CSS、JavaScript 文件等）。
    private static final String[] EXCLUDE_URL = {"/user-info/login","/user-info/register", "/user-info/login-out/",".png", ".js", ".css", ".jpg",".ico","/stacks"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //将传入的 ServletRequest 和 ServletResponse 强制转换为更具体的 HttpServletRequest 和 HttpServletResponse 类型，以便访问 HTTP 特有的属性和方法。
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpServletRequest.getRequestURI();
        log.info("LoginFilter requestURI: {}", requestURI);

        //如果请求的 URI 包含在 EXCLUDE_URL 数组中，或者请求方法是 OPTIONS（通常用于 CORS 预检请求），则直接放行请求，不进行后续的登录验证。
        if (excludeUrl(requestURI) || StringUtils.equalsIgnoreCase("OPTIONS", httpServletRequest.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = httpServletRequest.getHeader("token");
        token = StringUtils.isBlank(token) ? httpServletRequest.getHeader("Token") : token;
        UserEntity user = LoginUserCache.getGuavaCache(token);
        //如果用户未登录，设置 CORS 相关的响应头，并返回 JSON 格式的错误信息，告知客户端用户未登录。同时将响应状态码设置为 200（OK），因为某些前端框架可能会忽略非 2xx 状态码下的响应体内容。
        if (StringUtils.isBlank(token) || null == user) {
            log.warn("token为空，用户未登录");
            httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE");
            httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "trm_access_token,X-Requested-With,Content-Type,token");
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write("{\"responseCode\":" + ResponseCode.NO_AUTH.getCode() + ",\"responseDesc\":\"NOT LOGIN\"}");
            //确保所有缓冲的数据都被写入到目标目的地，并清空缓冲区。
            writer.flush();
            return;
        }
        user.setActivityTime(new Date());
        //如果用户已登录，更新其最后活动时间为当前时间，并将更新后的用户信息重新放入缓存中。
        LoginUserCache.putGuavaCache(token, user);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean excludeUrl(String url) {
        for (String excludeUrl : EXCLUDE_URL) {
            if (url.contains(excludeUrl)) {
                return true;
            }
        }
        return false;
    }
}
