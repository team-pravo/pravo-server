package com.pravo.pravo.global.auth.resolver;

import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.global.auth.annotation.AuthUser;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.exception.UnauthorizedException;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    public AuthUserArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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

        AuthenticateUser authenticatedUser = (AuthenticateUser) request.getAttribute(
            "authenticatedUser");

        if (authenticatedUser == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        // AuthenticateUser에 해당하는 Member가 없을 경우, 401 리턴
        Member member = memberRepository.findById(authenticatedUser.getMemberId())
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.UNAUTHORIZED));

        return authenticatedUser;
    }
}