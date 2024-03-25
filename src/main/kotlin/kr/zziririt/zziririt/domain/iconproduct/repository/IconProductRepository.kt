package kr.zziririt.zziririt.domain.iconproduct.repository

import kr.zziririt.zziririt.api.iconproduct.dto.IconSearchCondition
import kr.zziririt.zziririt.domain.iconproduct.model.IconProductEntity
import kr.zziririt.zziririt.infra.querydsl.iconproduct.dto.IconProductRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface IconProductRepository {
    fun searchBy(condition: IconSearchCondition, pageable: Pageable): PageImpl<IconProductRowDto>

    fun findByIdOrNull(id: Long): IconProductEntity?

    fun save(entity: IconProductEntity): IconProductEntity

    fun delete(entity: IconProductEntity)
}