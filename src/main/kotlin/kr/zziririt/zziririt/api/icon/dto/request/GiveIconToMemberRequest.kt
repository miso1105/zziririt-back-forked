package kr.zziririt.zziririt.api.icon.dto.request

import kr.zziririt.zziririt.domain.icon.model.IconBackOfficeDivision

data class GiveIconToMemberRequest (
    val iconId: Long,
    val memberId: Long,
    val division: IconBackOfficeDivision
)