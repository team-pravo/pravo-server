package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.dto.ProfileChangeRequestDTO;
import com.pravo.pravo.domain.member.service.MemberService;
import com.pravo.pravo.global.auth.annotation.AuthUser;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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
        @RequestHeader("Authorization") String token
    ) {
        return ApiResponseDto.success(memberService.logout(token));
    }

    @GetMapping("/member")
    public ApiResponseDto<MyPageResponseDTO> getMyPage(
        @AuthUser AuthenticateUser authenticateUser) {
        return ApiResponseDto.success(
            memberService.fetchMemberById(authenticateUser.getMemberId()));
    }

    @PatchMapping(value = "/member/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<MyPageResponseDTO> updateNameAndProfileImageUrl(
        @AuthUser AuthenticateUser authenticateUser,
        @ModelAttribute ProfileChangeRequestDTO profileChangeRequestDTO
    ) {
        return ApiResponseDto.success(
            memberService.updateNameAndProfileImageUrl(authenticateUser.getMemberId(),
                profileChangeRequestDTO.name(), profileChangeRequestDTO.file()));
    }
}
