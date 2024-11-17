package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.service.MemberService;
import com.pravo.pravo.global.common.ApiResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController implements MemberApi {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/logout")
    public ApiResponseDto<String> logoutPage(
        @Parameter(hidden = true) @RequestHeader("Authorization") String token
    ) {
        return ApiResponseDto.success(memberService.logout(token));
    }

    @GetMapping("/member/{memberId}")
    public ApiResponseDto<MyPageResponseDTO> myPage(
        @Parameter(hidden = true) @RequestHeader("Authorization") String token,
        @PathVariable("memberId") long memberId) {
        return ApiResponseDto.success(memberService.fetchMemberById(memberId));
    }
}
