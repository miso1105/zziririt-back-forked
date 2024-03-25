package kr.zziririt.zziririt.api.iconproduct.dto.response

import kr.zziririt.zziririt.domain.iconproduct.model.IconProductEntity
import kr.zziririt.zziririt.domain.iconproduct.model.SaleStatus

data class IconProductResponse(
    val iconProductId: Long,
    val price: Long,
    val iconQuantity: Int,
    val saleStatus: SaleStatus,
    val iconId: Long,
) {
    companion object {
        fun from(iconProductEntity: IconProductEntity): IconProductResponse = IconProductResponse(
            iconProductId = iconProductEntity.id,
            price = iconProductEntity.price,
            iconQuantity = iconProductEntity.iconQuantity,
            saleStatus = iconProductEntity.saleStatus,
            iconId = iconProductEntity.icon.id
        )
    }
}