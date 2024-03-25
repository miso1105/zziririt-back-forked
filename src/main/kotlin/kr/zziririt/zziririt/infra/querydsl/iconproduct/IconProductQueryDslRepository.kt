package kr.zziririt.zziririt.infra.querydsl.iconproduct

import kr.zziririt.zziririt.api.iconproduct.dto.IconSearchCondition
import kr.zziririt.zziririt.infra.querydsl.iconproduct.dto.IconProductRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface IconProductQueryDslRepository {
    fun searchBy(condition: IconSearchCondition, pageable: Pageable): PageImpl<IconProductRowDto>
}