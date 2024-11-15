package com.pravo.pravo.domain.member.dto;

import com.pravo.pravo.domain.member.model.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MyPageResponseDTO(
    @Schema(description = "멤버 이름", example = "막강한 문어 710")
    String name,
    @Schema(description = "프로필이미지 URL", example = "profileImageUrl")
    String profileImageUrl
) {

    public static MyPageResponseDTO of(Member member) {
        return new MyPageResponseDTO(
            member.getName(),
            member.getProfileImageUrl()
        );
    }
}
