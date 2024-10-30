package com.pravo.pravo.domain.promise.controller

import com.pravo.pravo.domain.promise.dto.request.PromiseSearchDto
import com.pravo.pravo.domain.promise.dto.response.PromiseResponseDto
import com.pravo.pravo.domain.promise.service.PromiseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/promise")
class PromiseController(
    private val promiseService: PromiseService,
) : PromiseApi {
    @GetMapping
    override fun getPromisesByMember(
        @RequestParam memberId: Long,
        @ModelAttribute request: PromiseSearchDto?,
    ): List<PromiseResponseDto> {
        // TODO: 토큰 기반 유저 인증 추가
        return promiseService.getPromisesByMember(memberId, request?.startedAt, request?.endedAt)
    }
}
