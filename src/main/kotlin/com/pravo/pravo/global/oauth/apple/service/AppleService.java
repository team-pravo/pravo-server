package com.pravo.pravo.global.oauth.apple.service;

import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import com.pravo.pravo.global.oauth.apple.AppleIdentityTokenVerifier;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AppleService {

    private final AppleIdentityTokenVerifier appleIdentityTokenVerifier;

    public AppleService(AppleIdentityTokenVerifier appleIdentityTokenVerifier) {
        this.appleIdentityTokenVerifier = appleIdentityTokenVerifier;
    }

    public LoginRequestDTO fetchAppleMemberId(String identityToken) {
        Claims claims = appleIdentityTokenVerifier.verifyIdentityToken(identityToken);
        String socialId = claims.getSubject(); // get userId from token
        if (socialId == null) {
            throw new IllegalArgumentException("Social ID is null. Invalid identity token.");
        }
        return new LoginRequestDTO(socialId);
    }
}
