package kr.zziririt.zziririt.api.member.dto.request

import kr.zziririt.zziririt.domain.member.model.ChangeDivision

data class ChangeMemberPointRequest(
    val memberId: Long,
    val pointChangeType: ChangeDivision,
    val value: Long
)
