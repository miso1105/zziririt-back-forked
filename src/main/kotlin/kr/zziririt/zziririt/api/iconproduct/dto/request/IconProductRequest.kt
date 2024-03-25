package kr.zziririt.zziririt.api.iconproduct.dto.request

import kr.zziririt.zziririt.domain.icon.model.IconEntity
import kr.zziririt.zziririt.domain.iconproduct.model.IconProductEntity
import kr.zziririt.zziririt.domain.iconproduct.model.SaleStatus

data class IconProductRequest(
    val iconId: Long,
    val price: Long,
    val iconQuantity: Int,
    val saleStatus: SaleStatus,
) {
    fun toEntity(icon: IconEntity): IconProductEntity = IconProductEntity(
        icon = icon,
        price = price,
        iconQuantity = iconQuantity,
        saleStatus = saleStatus,
    )
}