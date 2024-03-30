package kr.zziririt.zziririt.api.point.service

import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.point.model.PointHistoryEntity
import kr.zziririt.zziririt.domain.point.repository.PointHistoryRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointHistoryService (
    private val pointHistoryRepository: PointHistoryRepository,
    private val memberRepository: SocialMemberRepository,
) {

    @Transactional
    fun regPointHistory(changeAmount: Long, reason: String, member: SocialMemberEntity) {
        val memberCheck = memberRepository.findByIdOrNull(member.id!!)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        val history = PointHistoryEntity(
            changeAmount = changeAmount,
            reason = reason,
            member = memberCheck
        )

        pointHistoryRepository.save(history)
    }
}