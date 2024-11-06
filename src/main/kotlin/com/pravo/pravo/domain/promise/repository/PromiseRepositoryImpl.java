package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.model.Promise;
import static com.pravo.pravo.domain.promise.model.QPromise.promise;
import static com.pravo.pravo.domain.promise.model.QPromiseRole.promiseRole;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
import java.util.List;

public class PromiseRepositoryImpl implements PromiseRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PromiseRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Promise> getPromisesByMemberIdAndStartedAtAndEndedAt(Long memberId, LocalDate startedAt, LocalDate endedAt) {
        return queryFactory
            .selectFrom(promise)
            .join(promise.promiseRoles, promiseRole)
            .where(
                memberIdEquals(memberId),
                promiseDateAfter(startedAt),
                promiseDateBefore(endedAt)
            )
            .fetch();
    }

    private BooleanExpression memberIdEquals(Long memberId) {
        return memberId != null ? promiseRole.member.id.eq(memberId) : null;
    }

    private BooleanExpression promiseDateAfter(LocalDate startedAt) {
        return startedAt != null ? promise.promiseDate.goe(startedAt.atStartOfDay()) : null;
    }

    private BooleanExpression promiseDateBefore(LocalDate endedAt) {
        return endedAt != null ? promise.promiseDate.loe(endedAt.atTime(LocalTime.MAX)) : null;
    }
}
