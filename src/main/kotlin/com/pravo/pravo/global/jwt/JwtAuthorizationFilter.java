package com.pravo.pravo.global.jwt;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pravo.pravo.global.common.ApiResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;


public class JwtAuthorizationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final String[] whiteListUris = new String[]{"/api/login", "/api/login/**",
        "/api-docs/**", "/swagger-ui/**", "/actuator/**", "/api/admin/**"}; //URLs do not need authorized

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider,
        RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (whiteListCheck(httpServletRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        if (!isContainToken(httpServletRequest)) {
            sendErrorResponse(httpServletResponse,
                ApiResponseDto.error("인증되지 않은 사용자입니다", HttpStatus.UNAUTHORIZED.value(), "E01"));
            return;
        }

        try {
            String token = getToken(httpServletRequest);

            String isLogout = (String) redisTemplate.opsForValue().get(token);

            // 로그아웃이 없는(되어 있지 않은) 경우 해당 토큰은 정상적으로 작동하기
            if (ObjectUtils.isEmpty(isLogout)) {
                AuthenticateUser authenticateUser = getAuthenticateUser(token);
                httpServletRequest.setAttribute("authenticatedUser", authenticateUser);
                chain.doFilter(request, response);
            } else {
                sendErrorResponse(httpServletResponse,
                    ApiResponseDto.error("로그아웃된 토큰입니다", HttpStatus.UNAUTHORIZED.value(), "E01"));
            }
        } catch (JsonParseException e) {
            log.error("JsonParseException" + e.getMessage());
            sendErrorResponse(httpServletResponse,
                ApiResponseDto.error("잘못된 요청입니다", HttpStatus.BAD_REQUEST.value(), "E01"));
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.error("JwtException" + e.getMessage());
            sendErrorResponse(httpServletResponse,
                ApiResponseDto.error("유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED.value(), "E01"));
        } catch (ExpiredJwtException e) {
            log.error("JwtTokenExpired" + e.getMessage());
            sendErrorResponse(httpServletResponse,
                ApiResponseDto.error("유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED.value(), "E01"));
        }
    }

    private boolean whiteListCheck(String uri) {
        return PatternMatchUtils.simpleMatch(whiteListUris, uri);
    }

    private boolean isContainToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization != null && authorization.startsWith("Bearer ");
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization.substring(7);
    }

    private AuthenticateUser getAuthenticateUser(String token) throws JsonProcessingException {
        String claimsSubject = jwtTokenProvider.extractSubject(token);
        long memberId = Long.parseLong(claimsSubject);
        log.info("Parsed memberId: {}", memberId);
        return new AuthenticateUser(memberId);
    }

    private void sendErrorResponse(HttpServletResponse response, ApiResponseDto<?> errorResponse)
        throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorResponse.getStatus());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
