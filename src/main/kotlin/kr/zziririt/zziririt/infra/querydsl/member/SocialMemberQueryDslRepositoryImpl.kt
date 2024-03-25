package kr.zziririt.zziririt.infra.querydsl.member

import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.model.QLoginHistoryEntity
import kr.zziririt.zziririt.domain.member.model.QSocialMemberEntity
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class SocialMemberQueryDslRepositoryImpl : SocialMemberQueryDslRepository, QueryDslSupport() {

    private val member = QSocialMemberEntity.socialMemberEntity
    private val loginHistory = QLoginHistoryEntity.loginHistoryEntity

    override fun findSleeperCandidatesMemberId(): List<Long> {
        val baseSleeperTime = LocalDateTime.now().minusYears(1)

        return queryFactory
            .select(member.id)
            .from(member)
            .leftJoin(loginHistory)
            .on(member.id.eq(loginHistory.memberId.id), loginHistory.createdAt.goe(baseSleeperTime))
            .where(loginHistory.memberId.isNull)
            .fetch()
    }

    override fun bulkUpdateMemberStatusToSleeper(memberIdList: List<Long>) {
        queryFactory
            .update(member)
            .set(member.memberStatus, MemberStatus.SLEEPER)
            .where(member.id.`in`(memberIdList))
            .execute()
    }
}