package com.pravo.pravo.global.jwt;

public class JwtTokens {

    private String accessToken;
    private String grantType;
    private Long expiresIn;

    public JwtTokens(String accessToken, String grantType, Long expiresIn) {
        this.accessToken = accessToken;
        this.grantType = grantType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public static JwtTokens of(String accessToken, String grantType, Long expiresIn) {
        return new JwtTokens(accessToken, grantType, expiresIn);
    }
}