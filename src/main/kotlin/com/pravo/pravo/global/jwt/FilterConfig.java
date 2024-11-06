package com.pravo.pravo.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class FilterConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    ObjectMapper objectMapper;

    public FilterConfig(JwtTokenProvider jwtTokenProvider,
        RedisTemplate<String, String> redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilter() {
        FilterRegistrationBean<JwtAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        JwtAuthorizationFilter filter = new JwtAuthorizationFilter(jwtTokenProvider, redisTemplate, objectMapper);

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}