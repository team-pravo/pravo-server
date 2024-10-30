package com.pravo.pravo.domain.member.controller;

import com.pravo.pravo.domain.member.dto.LoginDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginPage(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok().body(memberService.login(loginDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutPage(HttpServletRequest request) {

        return ResponseEntity.ok().body(memberService.logout(request));
    }
}
