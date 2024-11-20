package com.pravo.pravo.global.oauth.controller;

import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.service.MemberService;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.oauth.apple.service.AppleService;
import com.pravo.pravo.global.oauth.domain.SocialLoginRequestToken;
import com.pravo.pravo.global.oauth.domain.enums.SocialProvider;
import com.pravo.pravo.global.oauth.kakao.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final AppleService appleService;

    public AuthController(MemberService memberService, KakaoService kakaoService,
        AppleService appleService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
        this.appleService = appleService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginPage(
        @RequestBody LoginRequestDTO loginRequestDto) {
        return ResponseEntity.ok().body(memberService.login(loginRequestDto));
        //TODO: 삭제 필요, 테스트용 임시 로그인 API
    }

    @PostMapping("/login/{socialProvider}")
    public ApiResponseDto<LoginResponseDTO> login(
        @PathVariable SocialProvider socialProvider,
        @RequestBody SocialLoginRequestToken socialLoginRequestToken) {
        LoginRequestDTO loginRequestDTO = switch (socialProvider) {
            case kakao -> kakaoService.fetchKakaoMemberId(socialLoginRequestToken.getSocialToken());
            case apple -> appleService.fetchAppleMemberId(socialLoginRequestToken.getSocialToken());
        };
        return ApiResponseDto.success(memberService.login(loginRequestDTO));
    }
}
