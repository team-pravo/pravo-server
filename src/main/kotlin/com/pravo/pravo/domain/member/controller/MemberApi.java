package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "jwt")
@Tag(name = "Member", description = "멤버 API")
public interface MemberApi {

    @Operation(summary = "로그아웃", description = "로그아웃입니다")
    ApiResponseDto<String> logoutPage(
        @Parameter(hidden = true) String token
    );

    @Operation(summary = "마이페이지", description = "멤버 이름과 프로필 사진 URL 조회합니다")
    ApiResponseDto<MyPageResponseDTO> myPage(
        @Parameter(hidden = true) AuthenticateUser authenticateUser
    );

    @Operation(summary = "프로필 설정 페이지", description = "멤버 이름과 프로필 사진 변경합니다")
    ApiResponseDto<MyPageResponseDTO> updateNameAndProfileImageUrl(
        @Parameter(hidden = true) AuthenticateUser authenticateUser,
        String name,
        MultipartFile file
    );

}
