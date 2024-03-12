package kr.zziririt.zziririt.infra.querydsl.member


interface SocialMemberQueryDslRepository {

    fun findSleeperCandidatesMemberId() : List<Long>

    fun bulkUpdateMemberStatusToSleeper(memberIdList: List<Long>)
}