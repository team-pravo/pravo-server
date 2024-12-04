package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.MemberFineLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberPaymentLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberPointLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.dto.ProfileChangeRequestDTO;
import com.pravo.pravo.global.common.ApiResponseDto;
import com.pravo.pravo.global.jwt.AuthenticateUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@SecurityRequirement(name = "jwt")
@Tag(name = "Member", description = "멤버 API")
public interface MemberApi {

    @Operation(summary = "로그아웃", description = "로그아웃입니다")
    ApiResponseDto<String> logoutPage(
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

    @Operation(summary = "결제 내역", description = "멤버 결제 내역 조회합니다")
    ApiResponseDto<List<MemberPaymentLogResponseDTO>> getMemberPaymentLog(
        @Parameter(hidden = true) AuthenticateUser authenticateUser
    );

    @Operation(summary = "포인트 사용/적립 내역", description = "포인트 사용/적립 내역 조회합니다")
    ApiResponseDto<List<MemberPointLogResponseDTO>> getMemberPointLog(
        @Parameter(hidden = true) AuthenticateUser authenticateUser
    );

    @Operation(summary = "별금 내역", description = "별금 내역 조회합니다")
    ApiResponseDto<List<MemberFineLogResponseDTO>> getMemberFineLog(
        @Parameter(hidden = true) AuthenticateUser authenticateUser
    );
}
