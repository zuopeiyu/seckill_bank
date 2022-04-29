package com.csse.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse.mapper.LoginUserMapper;
import com.csse.service.LoginUserService;
import com.csse.utils.JwtUtil;
import com.csse.utils.RedisCache;
import com.csse.utils.SpringBeanFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    AuthenticationManager authenticationManager;


    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;
        Map<String, String[]> parameterMap = request.getParameterMap();
        //从表单中获取用户名和密码进行验证
        username = parameterMap.get("username")[0];
        password = parameterMap.get("password")[0];
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList()));

    }

    //认证成功之后执行
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //获取用户信息
        User userDetails = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = userDetails.getUsername();
        LoginUserService mapper = SpringBeanFactoryUtil.getBean(LoginUserService.class);
        com.csse.domain.User user = mapper.selectUser(username);

        //生成token
        long id = user.getId();
        String jwt = JwtUtil.createJWT(String.valueOf(id));
        //将用户信息存入redis
        RedisCache redisTemplate = SpringBeanFactoryUtil.getBean(RedisCache.class);
        redisTemplate.setCacheObject("login:" + String.valueOf(id), userDetails);
//        redisTemplate.boundValueOps("login:"+String.valueOf(id)).set(userDetails);
        System.out.println("登录成功:" + jwt);
        response.setHeader("token", jwt);
        response.getWriter().write("aslkdjlkasjdlaaaaaaaaaaaaaaaaaaaa");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("登录失败");
    }
}
