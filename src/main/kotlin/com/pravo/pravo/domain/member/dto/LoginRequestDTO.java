package com.pravo.pravo.domain.member.dto;

public record LoginRequestDTO(
    String socialId
) {
    public String getSocialId() {
        return socialId;
    }
}
