package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.service.MemberService;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.oauth.kakao.dto.KakaoLoginRequestDTO;
import com.pravo.pravo.global.oauth.kakao.service.KakaoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    public MemberController(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginPage(
        @RequestBody LoginRequestDTO loginRequestDto) {
        return ResponseEntity.ok().body(memberService.login(loginRequestDto));
        //TODO: 삭제 필요, 테스트용 임시 로그인 API
    }

    @PostMapping("/login/kakao")
    public ApiResponseDto<LoginResponseDTO> loginKakao(
        @RequestBody KakaoLoginRequestDTO kakaoLoginRequestDTO) {
        LoginRequestDTO loginRequestDto = kakaoService.fetchKakaoMemberId(
            kakaoLoginRequestDTO.getKakaoToken());
        return ApiResponseDto.success(memberService.login(loginRequestDto));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "jwt")
    public ApiResponseDto<String> logoutPage(
        @Parameter(hidden = true) @RequestHeader("Authorization") String token
    ) {
        return ApiResponseDto.success(memberService.logout(token));
    }
}
