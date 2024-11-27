package com.pravo.pravo.domain.home.service

import com.pravo.pravo.domain.home.dto.HomeResponseDto
import com.pravo.pravo.domain.home.dto.UpcomingPromiseDto
import com.pravo.pravo.domain.member.service.MemberService
import com.pravo.pravo.domain.promise.service.PromiseService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HomeFacade(
    private val memberService: MemberService,
    private val promiseService: PromiseService,
) {
    fun getHome(memberId: Long): HomeResponseDto {
        val member = memberService.getMemberById(memberId)
        val todayPromises =
            promiseService.getPromisesByMember(memberId, LocalDate.now(), LocalDate.now())

        val upcomingPromises =
            promiseService
                .getPromisesByMember(
                    memberId,
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(7),
                ).map { p ->
                    UpcomingPromiseDto(p.promiseDate, p.name)
                }.sortedBy { it.promiseDate }

        return HomeResponseDto(todayPromises, member.point, upcomingPromises)
    }
}
