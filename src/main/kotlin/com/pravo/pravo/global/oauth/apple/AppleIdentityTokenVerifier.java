package com.pravo.pravo.global.oauth.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleIdentityTokenVerifier {

    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";
    private static final String APPLE_ISS = "https://appleid.apple.com";

    @Value("${spring.apple.aud}")
    private String appleClientId;
    private final Map<String, PublicKey> applePublicKeys;

    public AppleIdentityTokenVerifier() throws Exception {
        this.applePublicKeys = loadApplePublicKeys();
    }

    private Map<String, PublicKey> loadApplePublicKeys() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jwkSet = mapper.readValue(new URL(APPLE_PUBLIC_KEYS_URL), Map.class);
        return ((List<Map<String, Object>>) jwkSet.get("keys")).stream().collect(Collectors.toMap(
            key -> key.get("kid").toString(),
            key -> {
                try {
                    BigInteger modulus = new BigInteger(1,
                        Base64.getUrlDecoder().decode((String) key.get("n")));
                    BigInteger exponent = new BigInteger(1,
                        Base64.getUrlDecoder().decode((String) key.get("e")));
                    return KeyFactory.getInstance("RSA")
                        .generatePublic(new RSAPublicKeySpec(modulus, exponent));
                } catch (Exception e) {
                    throw new RuntimeException("Error creating public key", e);
                }
            }
        ));
    }

    public Claims verifyIdentityToken(String identityToken) {
        return Jwts.parserBuilder()
            .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                @Override
                public PublicKey resolveSigningKey(io.jsonwebtoken.JwsHeader header,
                    Claims claims) {
                    return applePublicKeys.get(header.getKeyId());
                }
            })
            .requireIssuer(APPLE_ISS)
            .requireAudience(appleClientId)
            .build()
            .parseClaimsJws(identityToken)
            .getBody();
    }
}