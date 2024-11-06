package com.pravo.pravo.global.jwt;

public class AuthenticateUser {
    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }
    public AuthenticateUser(Long memberId) {
        this.memberId = memberId;
    }
}