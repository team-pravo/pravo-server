package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.LoginDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.service.MemberService;
import com.pravo.pravo.global.oauth.kakao.dto.KakaoLoginRequestDTO;
import com.pravo.pravo.global.oauth.kakao.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    public MemberController(MemberService memberService, KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginPage(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok().body(memberService.login(loginDTO));
        //TODO: 삭제 필요, 테스트용 임시 로그인 API
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponseDTO> loginKakao(
        @RequestBody KakaoLoginRequestDTO kakaoLoginRequestDTO) {
        LoginDTO loginDTO = kakaoService.fetchKakaoMemberId(kakaoLoginRequestDTO.getKakaoToken());
        return ResponseEntity.ok().body(memberService.login(loginDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutPage(HttpServletRequest request) {

        return ResponseEntity.ok().body(memberService.logout(request));
    }
}
