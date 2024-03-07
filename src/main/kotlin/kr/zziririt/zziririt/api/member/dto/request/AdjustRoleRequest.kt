package kr.zziririt.zziririt.api.member.dto.request

import jakarta.validation.constraints.NotNull
import kr.zziririt.zziririt.domain.member.model.MemberRole

data class AdjustRoleRequest(
    @field: NotNull
    val memberRole: MemberRole
)
