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
    @JsonProperty("profileImage")
    private String profileImage;

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
