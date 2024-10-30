package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import com.pravo.pravo.global.common.error.exception.UnauthorizedException;
import com.pravo.pravo.global.common.error.ErrorCode;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validateMemberById(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }
}
