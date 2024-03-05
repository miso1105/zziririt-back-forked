package kr.zziririt.zziririt.api.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class CommonResponse<T>(
    var code: Int,
    var message: String? = null,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var content: T? = null
) {
    companion object {
        fun of(code: Int) = CommonResponse<Nothing>(code)
        fun of(code: Int, message: String) = CommonResponse<Nothing>(code, message)
        fun <T> of(code: Int, content: T) = CommonResponse(code, content = content)
        fun <T> of(code: Int, message: String, content: T) = CommonResponse(code, message, content)
    }
}