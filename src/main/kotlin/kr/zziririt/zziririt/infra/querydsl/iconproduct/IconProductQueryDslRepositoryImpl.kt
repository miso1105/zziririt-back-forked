package kr.zziririt.zziririt.infra.querydsl.iconproduct

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import kr.zziririt.zziririt.api.iconproduct.dto.IconSearchCondition
import kr.zziririt.zziririt.domain.iconproduct.model.QIconProductEntity
import kr.zziririt.zziririt.domain.iconproduct.model.SaleStatus
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import kr.zziririt.zziririt.infra.querydsl.iconproduct.dto.IconProductRowDto
import kr.zziririt.zziririt.infra.querydsl.iconproduct.dto.QIconProductRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class IconProductQueryDslRepositoryImpl : IconProductQueryDslRepository, QueryDslSupport() {

    private val iconProduct = QIconProductEntity.iconProductEntity

    override fun searchBy(condition: IconSearchCondition, pageable: Pageable): PageImpl<IconProductRowDto> {
        val statusCondition = iconProduct.saleStatus.`in`(
            SaleStatus.PREPARE, SaleStatus.SALE, SaleStatus.SOLDOUT
        )

        val result = queryFactory
            .select(
                QIconProductRowDto(
                    iconProduct.icon.id,
                    iconProduct.price,
                    iconProduct.iconQuantity,
                    iconProduct.saleStatus
                )
            )
            .from(iconProduct)
            .where(
                saleStatusEq(condition.searchType)
            )
            .orderBy(*createOrderSpecifiers(pageable))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: Long = queryFactory
            .select(iconProduct.count())
            .from(iconProduct)
            .where(statusCondition)
            .fetchOne() ?: 0L

        return PageImpl(result, pageable, count)
    }

    private fun saleStatusEq(saleStatus: String?): BooleanExpression? {
        if (saleStatus.isNullOrEmpty()) {
            return null
        }
        return when (saleStatus) {
            SaleStatus.PREPARE.name -> iconProduct.saleStatus.eq(SaleStatus.PREPARE)
            SaleStatus.SALE.name -> iconProduct.saleStatus.eq(SaleStatus.SALE)
            SaleStatus.SOLDOUT.name -> iconProduct.saleStatus.eq(SaleStatus.SOLDOUT)
            else -> null
        }
    }

    private fun createOrderSpecifiers(pageable: Pageable): Array<OrderSpecifier<*>?> {
        return pageable.sort?.let { sort ->
            sort.mapNotNull { order ->
                val direction = if (order.isAscending) Order.ASC else Order.DESC
                when (order.property) {
                    "price" -> OrderSpecifier(direction, QIconProductEntity.iconProductEntity.price)
                    "iconQuantity" -> OrderSpecifier(direction, QIconProductEntity.iconProductEntity.iconQuantity)
                    else -> OrderSpecifier(direction, QIconProductEntity.iconProductEntity.createdAt)
                }
            }.toTypedArray()
        } ?: arrayOf()
    }
}