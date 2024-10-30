package com.pravo.pravo.global.common.error

enum class ErrorCode(
    val message: String,
    val status: Int,
    val code: String,
) {
    INTERNAL_SERVER_ERROR("Internal Server Error", 500, "E01"),
    BAD_REQUEST("Bad Request", 400, "E02"),
    UNAUTHORIZED("Unauthorized Member", 401, "E03"),
    FORBIDDEN("Forbidden", 403, "E04"),
    NOT_FOUND("Not Found", 404, "E05"),
    METHOD_NOT_ALLOWED("Method Not Allowed", 405, "E06"),
}
