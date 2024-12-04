package com.pravo.pravo.domain.promise.repository;

import static com.pravo.pravo.domain.promise.model.QPromise.promise;

import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto;
import com.pravo.pravo.domain.promise.dto.response.QPromiseResponseDto;
import com.pravo.pravo.domain.promise.model.QPromise;
import com.pravo.pravo.domain.promise.model.QPromiseRole;
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus;
import com.pravo.pravo.domain.promise.model.enums.RoleStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PromiseRepositoryImpl implements PromiseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PromiseRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PromiseResponseDto> getPromisesByMemberIdAndStartedAtAndEndedAt(Long memberId,
        LocalDate startedAt, LocalDate endedAt) {
        QPromiseRole promiseRole = QPromiseRole.promiseRole;  // 기본 alias
        QPromiseRole organizer = new QPromiseRole("organizer");  // 모임장용 alias
        QPromise promise = QPromise.promise;

        return queryFactory
            .select(new QPromiseResponseDto(
                promise.id,
                promise.name,
                promise.scheduledAt,
                promise.location,
                promise.status,
                organizer.member.name,
                organizer.member.profileImageUrl
            ))
            .from(promise)
            .join(promiseRole).on(promiseRole.promise.eq(promise))
            .leftJoin(organizer).on(
                organizer.promise.eq(promise)
                    .and(organizer.role.eq(RoleStatus.ORGANIZER))
            )
            .where(
                promiseRole.member.id.eq(memberId),
                scheduledAtAfter(startedAt),
                scheduledAtBefore(endedAt),
                promise.status.ne(PromiseStatus.PENDING)
            )
            .orderBy(promise.scheduledAt.asc())
            .fetch();
    }

    private BooleanExpression scheduledAtAfter(LocalDate startedAt) {
        return startedAt != null ? promise.scheduledAt.goe(startedAt.atStartOfDay()) : null;
    }

    private BooleanExpression scheduledAtBefore(LocalDate endedAt) {
        return endedAt != null ? promise.scheduledAt.loe(endedAt.atTime(LocalTime.MAX)) : null;
    }
}
