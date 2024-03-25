package kr.zziririt.zziririt.infra.querydsl.member

import kr.zziririt.zziririt.infra.querydsl.member.dto.GetMyIconsDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface MemberIconQueryDslRepository {

    fun getMyIcons(pageable: Pageable) : PageImpl<GetMyIconsDto>
}