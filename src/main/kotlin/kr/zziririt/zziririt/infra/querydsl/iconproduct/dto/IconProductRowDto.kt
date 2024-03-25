package kr.zziririt.zziririt.infra.querydsl.iconproduct.dto

import com.querydsl.core.annotations.QueryProjection
import kr.zziririt.zziririt.domain.iconproduct.model.SaleStatus
import java.io.Serializable

data class IconProductRowDto @QueryProjection constructor(
    val iconId: Long,
    val price: Long,
    val iconQuantity: Int,
    val saleStatus: SaleStatus
) : Serializable