package com.pravo.pravo.global.error

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
    IMAGE_EXTENSION_ERROR("이미지 확장자는 jpg, png, webp만 가능합니다.", 400, "E07"),
    IMAGE_SIZE_ERROR("이미지 사이즈는 5MB를 넘을 수 없습니다.", 400, "E08"),
    NAME_EXIST_ERROR("Name already exists", 409, "E09"),
}
