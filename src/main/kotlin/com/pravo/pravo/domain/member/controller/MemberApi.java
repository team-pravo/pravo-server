package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.dto.ProfileChangeRequestDTO;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "jwt")
@Tag(name = "Member", description = "멤버 API")
public interface MemberApi {

    @Operation(summary = "로그아웃", description = "로그아웃입니다")
    ApiResponseDto<Void> logoutPage(
        @Parameter(hidden = true) String token
    );

    @Operation(summary = "마이페이지", description = "멤버 이름과 프로필 사진 URL 조회합니다")
    ApiResponseDto<MyPageResponseDTO> getMyPage(
        @Parameter(hidden = true) AuthenticateUser authenticateUser
    );

    @Operation(summary = "프로필 설정 페이지", description = "멤버 이름이나 프로필 사진을 변경할 수 있습니다. (지원 사진 파일 형식: JPEG,JPG,PNG,WEBP / 최대 크기: 5MB)")
    ApiResponseDto<MyPageResponseDTO> updateNameAndProfileImageUrl(
        @Parameter(hidden = true) AuthenticateUser authenticateUser,
        ProfileChangeRequestDTO profileChangeRequestDTO
    );

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴합니다")
    ApiResponseDto<Void> withdrawMember(
        @Parameter(hidden = true) AuthenticateUser authenticateUser,
        @Parameter(hidden = true) String token
    );

}
