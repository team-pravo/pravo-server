package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.model.Promise;
import java.time.LocalDate;
import java.util.List;

public interface PromiseRepositoryCustom {
    List<Promise> getPromisesByMemberIdAndStartedAtAndEndedAt(Long memberId, LocalDate startedAt, LocalDate endedAt);
}
