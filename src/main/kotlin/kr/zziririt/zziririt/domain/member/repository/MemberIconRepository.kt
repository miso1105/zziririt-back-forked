package kr.zziririt.zziririt.domain.member.repository

import kr.zziririt.zziririt.domain.member.model.MemberIconEntity
import kr.zziririt.zziririt.infra.querydsl.member.dto.GetMyIconsDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable


interface MemberIconRepository {

    fun save(entity: MemberIconEntity): MemberIconEntity

    fun delete(entity: MemberIconEntity)

    fun findByIdOrNull(id: Long): MemberIconEntity?

    fun getMyIcons(pageable: Pageable): PageImpl<GetMyIconsDto>

    fun existsByMemberIdAndIconId(memberId: Long, iconId: Long): Boolean

    fun findByMemberIdAndIconId(memberId:Long, iconId:Long) : MemberIconEntity?

}