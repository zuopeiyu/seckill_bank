package com.csse.filter;

import com.alibaba.fastjson.JSONObject;
import com.csse.domain.UserEntity;
import com.csse.service.LoginUserService;
import com.csse.utils.JwtUtil;
import com.csse.utils.RedisCache;
import com.csse.utils.SpringBeanFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
//        username = request.getHeader("username");
//        password = request.getHeader("password");
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
        UserEntity userEntity = mapper.selectUser(username);

        //生成token
        long id = userEntity.getId();
        String jwt = JwtUtil.createJWT(String.valueOf(id));
        //将用户信息存入redis
        RedisCache redisTemplate = SpringBeanFactoryUtil.getBean(RedisCache.class);
        redisTemplate.setCacheObject("login:" + String.valueOf(id), userDetails);
//        redisTemplate.boundValueOps("login:"+String.valueOf(id)).set(userDetails);
        System.out.println("登录成功:" + jwt);
        response.setHeader("token", jwt);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",jwt);
        jsonObject.put("userId", userEntity.getId());
        response.getWriter().write(String.valueOf(jsonObject));

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("登录失败");
    }
}
