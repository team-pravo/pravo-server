package com.pravo.pravo.global.auth.resolver;

import com.pravo.pravo.global.auth.annotation.AuthUser;
import com.pravo.pravo.global.common.error.ErrorCode;
import com.pravo.pravo.global.common.error.exception.UnauthorizedException;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class)
            && parameter.getParameterType().equals(AuthenticateUser.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        AuthenticateUser authenticatedUser = (AuthenticateUser) request.getAttribute("authenticatedUser");

        if (authenticatedUser == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        return authenticatedUser;
    }
}