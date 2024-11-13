package com.pravo.pravo.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pravo.pravo.global.jwt.JwtTokens;

public class LoginResponseDTO {

    @JsonProperty("jwtTokens")
    private JwtTokens jwtTokens;

    @JsonProperty("memberId")
    private Long memberId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("profileImageUrl")
    private String profileImageUrl;

    public JwtTokens getJwtTokens() {
        return jwtTokens;
    }

    public void setJwtTokens(JwtTokens jwtTokens) {
        this.jwtTokens = jwtTokens;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
