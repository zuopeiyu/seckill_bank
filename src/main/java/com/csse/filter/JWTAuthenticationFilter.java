package com.csse.filter;

import com.csse.utils.JwtUtil;
import com.csse.utils.RedisCache;
import com.csse.utils.SpringBeanFactoryUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();
    @Autowired
    RedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (isProtectedUrl(request)) {
            //校验token
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
            if (Objects.isNull(authenticationToken)) {
                throw new RuntimeException("Token校验失败");
            } else {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                //放行
                filterChain.doFilter(request, response);
            }

        } else {
            filterChain.doFilter(request, response);
        }

    }

    private boolean isProtectedUrl(HttpServletRequest request) {

        return pathMatcher.match("/goods/*", request.getServletPath());
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader("token");
        try {
            Claims claims = JwtUtil.parseJWT(token);
            //从redis中获取数据
            String id = claims.getSubject();
            String key = "login:" + id;
            RedisCache redisTemplate = SpringBeanFactoryUtil.getBean(RedisCache.class);
//            User userDetails = redisTemplate.getCacheObject(key);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("text"));
            //    User userDetails =new User(null,null,authorities);
//            if (Objects.isNull(userDetails)){
//                throw  new RuntimeException("用户未登录");
//            }
//            Collection<GrantedAuthority> authorities = userDetails.getAuthorities();
            return new UsernamePasswordAuthenticationToken(null, null, authorities);

        } catch (Exception e) {
            e.printStackTrace();
            //TODO异常处理
            throw new RuntimeException("Token校验失败");
        }
    }
}
