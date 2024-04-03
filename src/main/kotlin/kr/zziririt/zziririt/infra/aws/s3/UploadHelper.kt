package kr.zziririt.zziririt.infra.aws.s3

import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.RestApiException

fun String.isImageFileOrThrow() {
    val fileExtension = this.split(".").let { it[it.lastIndex] }
    check(fileExtension.contains("png") or fileExtension.contains("jpeg") or fileExtension.contains("jpg")) {
        throw RestApiException(ErrorCode.NOT_IMAGE_FILE_EXTENSION)
    }
}