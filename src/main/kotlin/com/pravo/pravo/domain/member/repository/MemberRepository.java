package com.pravo.pravo.domain.member.repository;

import com.pravo.pravo.domain.member.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialId(String socialId);

    boolean existsByName(String randomName);
}
