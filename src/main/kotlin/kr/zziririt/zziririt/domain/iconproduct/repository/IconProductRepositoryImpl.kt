package kr.zziririt.zziririt.domain.iconproduct.repository

import kr.zziririt.zziririt.api.iconproduct.dto.IconSearchCondition
import kr.zziririt.zziririt.domain.iconproduct.model.IconProductEntity
import kr.zziririt.zziririt.infra.jpa.iconproduct.IconProductJpaRepository
import kr.zziririt.zziririt.infra.querydsl.iconproduct.IconProductQueryDslRepository
import kr.zziririt.zziririt.infra.querydsl.iconproduct.dto.IconProductRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class IconProductRepositoryImpl(
    private val iconProductQueryDslRepository: IconProductQueryDslRepository,
    private val iconProductJpaRepository: IconProductJpaRepository,
) : IconProductRepository {
    override fun searchBy(condition: IconSearchCondition, pageable: Pageable): PageImpl<IconProductRowDto> =
        iconProductQueryDslRepository.searchBy(condition, pageable)

    override fun findByIdOrNull(id: Long): IconProductEntity? = iconProductJpaRepository.findByIdOrNull(id)

    override fun save(entity: IconProductEntity): IconProductEntity = iconProductJpaRepository.save(entity)

    override fun delete(entity: IconProductEntity) = iconProductJpaRepository.delete(entity)
}