package com.pravo.pravo.domain.promise.repository;

import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto;
import com.pravo.pravo.domain.promise.dto.response.QPromiseResponseDto;
import static com.pravo.pravo.domain.promise.model.QPromise.promise;
import static com.pravo.pravo.domain.promise.model.QPromiseRole.promiseRole;

import com.pravo.pravo.domain.promise.model.enums.RoleStatus;
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
    public List<PromiseResponseDto> getPromisesByMemberIdAndStartedAtAndEndedAt(Long memberId, LocalDate startedAt, LocalDate endedAt) {
        return queryFactory
            .select(new QPromiseResponseDto(
                promise.id,
                promise.name,
                promise.promiseDate,
                promise.location,
                promise.status,
                promiseRole.member.name,
                promiseRole.member.profileImage
            ))
            .from(promise)
            .join(promise.promiseRoles, promiseRole).on(promiseRole.role.eq(RoleStatus.ORGANIZER)).fetchJoin()
            .leftJoin(promiseRole.member).fetchJoin()
            .where(
                memberIdEquals(memberId),
                promiseDateAfter(startedAt),
                promiseDateBefore(endedAt)
            )
            .orderBy(promise.promiseDate.asc())
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
