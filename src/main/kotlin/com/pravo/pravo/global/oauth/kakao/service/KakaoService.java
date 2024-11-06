package com.pravo.pravo.global.oauth.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoService {

    private final String UserInfoUri = "https://kapi.kakao.com/v2/user/me";

    public LoginRequestDTO fetchKakaoMemberId(String kakaoToken) {

        //Http 요청
        WebClient wc = WebClient.create(UserInfoUri);
        //TODO: WebClient vs OpenFeign
        String response = wc.post()
            .uri(UserInfoUri)
            .header("Authorization", "Bearer " + kakaoToken)
            .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            .retrieve()
            .bodyToMono(String.class)
            .block();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            String socialId = String.valueOf(responseMap.get("id"));
            return new LoginRequestDTO(socialId);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
