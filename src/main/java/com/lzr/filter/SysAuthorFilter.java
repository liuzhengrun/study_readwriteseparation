package com.lzr.filter;

import com.lzr.response.HttpResponse;
import com.lzr.response.StaticData;
import com.lzr.response.enums.ResponseResultEnums;
import com.lzr.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Log4j2
@WebFilter(urlPatterns = {"/*"})
public class SysAuthorFilter implements Filter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filter-init！！！");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (uri.equals("/main/home")) {
            chain.doFilter(request, response);
        } else {
            String token = req.getHeader("token");
            if (StringUtils.isBlank(token)) {
                returnResponse(res, ResponseResultEnums.TOKEN_NOT_EXIST);
            }
            String userToken = StaticData.SYS_USER_AUTH_TOKEN_PREFIX + token;
            if (!stringRedisTemplate.hasKey(userToken)) {
                returnResponse(res, ResponseResultEnums.TOKEN_CERTIFICATION_FAIL);
            } else {
                stringRedisTemplate.expire(userToken, 600000, TimeUnit.MINUTES);
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        log.info("filter-destroy！！！");
    }

    private void returnResponse(HttpServletResponse response, ResponseResultEnums resultCodeEnum) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JsonUtils.Object2json(HttpResponse.fail(resultCodeEnum.getCode(),resultCodeEnum.getMsg())));
        out.flush();
        out.close();
    }
}
