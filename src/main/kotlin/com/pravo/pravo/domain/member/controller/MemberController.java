package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.service.MemberService;
import com.pravo.pravo.global.auth.annotation.AuthUser;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping(value = "/member/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<MyPageResponseDTO> updateNameAndProfileImageUrl(
        @Parameter(hidden = true) @RequestHeader("Authorization") String token,
        @Parameter(hidden = true) @AuthUser AuthenticateUser authenticateUser,
        @RequestPart("name") String name,
        @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ApiResponseDto.success(
            memberService.updateNameAndProfileImageUrl(authenticateUser, name, file));
    }
}
