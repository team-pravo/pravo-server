package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface PromiseRepositoryCustom {
    List<PromiseResponseDto> getPromisesByMemberIdAndStartedAtAndEndedAt(Long memberId, LocalDate startedAt, LocalDate endedAt);
}