package com.pravo.pravo.global.external.s3

import com.pravo.pravo.global.common.ApiResponseDto
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

// TODO S3 활용 로직 개발시 삭제예정
@RestController
@RequestMapping("/api/s3")
class S3Controller(
    private val s3Service: S3Service,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun uploadTest(
        @RequestPart file: MultipartFile,
    ): ApiResponseDto<String> = ApiResponseDto.success(s3Service.uploadFile(file, "profile-image"))

    @GetMapping
    fun testException(): Nothing = throw RuntimeException()
}
