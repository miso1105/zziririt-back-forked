package kr.zziririt.zziririt.api.iconproduct.dto.request

import kr.zziririt.zziririt.domain.iconproduct.model.SaleStatus

data class ChangeSaleStatusRequest(
    val saleStatus: SaleStatus,
)