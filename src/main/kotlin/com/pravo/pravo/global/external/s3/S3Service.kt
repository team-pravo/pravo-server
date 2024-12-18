@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.pravo.pravo.global.external.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.pravo.pravo.global.error.ErrorCode
import com.pravo.pravo.global.error.exception.BaseException
import com.pravo.pravo.global.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class S3Service(
    private val amazonS3Client: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    @Value("\${spring.member.default-profile-image-url}") private val defaultProfileImageUrl: String,
) {
    val logger = logger()

    companion object {
        val IMAGE_EXTENSIONS: List<String> =
            listOf("image/jpeg", "image/png", "image/jpg", "image/webp")
        val MAX_FILE_SIZE: Long = 5 * 1024 * 1024L
    }

    fun uploadFile(
        file: MultipartFile,
        folder: String,
    ): String {
        validateExtension(file)
        validateFileSize(file)

        val fileExtension = file.originalFilename?.substringAfterLast(".", "")
        val fileName = "${UUID.randomUUID()}.$fileExtension"

        val objectMetadata =
            ObjectMetadata().apply {
                contentType = file.contentType
                contentLength = file.size
            }

        amazonS3Client.putObject(
            "$bucket/$folder",
            fileName,
            file.inputStream,
            objectMetadata,
        )
        return amazonS3Client.getUrl("$bucket/$folder", fileName).toString()
    }

    private fun validateExtension(image: MultipartFile) {
        if (!IMAGE_EXTENSIONS.contains(image.contentType)) {
            println(image.contentType)
            throw BaseException(ErrorCode.IMAGE_EXTENSION_ERROR)
        }
    }

    private fun validateFileSize(image: MultipartFile) {
        if (image.size > MAX_FILE_SIZE) {
            throw BaseException(ErrorCode.IMAGE_SIZE_ERROR)
        }
    }

    fun deleteFile(imageUrl: String) {
        if (imageUrl == defaultProfileImageUrl) {
            logger.info("Skipping deletion as the image is the default profile image.")
            return
        }
        val fileKey = imageUrl.substringAfter("$bucket/")
        if (!amazonS3Client.doesObjectExist(bucket, fileKey)) {
            logger.warn("Image file does not exist in AWS S3 ")
        }
        amazonS3Client.deleteObject(bucket, fileKey)
    }
}
