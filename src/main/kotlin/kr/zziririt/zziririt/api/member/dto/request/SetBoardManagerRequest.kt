package kr.zziririt.zziririt.api.member.dto.request

import jakarta.validation.constraints.NotNull

data class SetBoardManagerRequest(
    @field: NotNull
    val memberId : Long
)
