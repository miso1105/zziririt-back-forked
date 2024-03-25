package kr.zziririt.zziririt.infra.querydsl.member

import kr.zziririt.zziririt.domain.member.model.QMemberIconEntity
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import kr.zziririt.zziririt.infra.querydsl.member.dto.GetMyIconsDto
import kr.zziririt.zziririt.infra.querydsl.member.dto.QGetMyIconsDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class MemberIconQueryDslRepositoryImpl : MemberIconQueryDslRepository, QueryDslSupport() {

    private val memberIcon = QMemberIconEntity.memberIconEntity
    override fun getMyIcons(pageable: Pageable): PageImpl<GetMyIconsDto> {

        val result = queryFactory
            .select(
                QGetMyIconsDto(
                    memberIcon.member.id,
                    memberIcon.icon.id,
                    memberIcon.createdAt
                )
            )
            .from(memberIcon)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(memberIcon.count())
            .from(memberIcon)
            .fetchOne() ?: 0L

        return PageImpl(result, pageable, count)
    }
}