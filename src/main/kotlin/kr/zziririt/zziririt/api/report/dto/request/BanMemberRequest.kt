package kr.zziririt.zziririt.api.report.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.report.model.BannedHistoryEntity
import java.time.LocalDateTime

data class BanMemberRequest(
    val memberId: Long,
    @field: Min(1, message = "정지 기간은 최소 {min}시간부터 설정해야 합니다.")
    @field: Max(100000, message = "정지 기간은 최대 {max}시간으로 설정해야 합니다.")
    val period: Long,
    @field: Size(min = 1, max = 500, message = "정지 사유는 {min}글자 이상, {max}글자 이하여야 합니다.")
    val reason: String,
) {
    fun toEntity(member: SocialMemberEntity): BannedHistoryEntity = BannedHistoryEntity(
        bannedReason = reason,
        bannedStartDate = LocalDateTime.now(),
        bannedPeriod = period,
        bannedEndDate = LocalDateTime.now().plusHours(period),
        bannedMemberId = member,
    )
}
