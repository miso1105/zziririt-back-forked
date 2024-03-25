package kr.zziririt.zziririt.api.iconproduct.dto

import kr.zziririt.zziririt.api.iconproduct.dto.valid.ValidSaleStatusType
import org.springframework.validation.annotation.Validated

@Validated
data class IconSearchCondition(
    @field:ValidSaleStatusType
    val searchType: String?,
    val searchTerm: String?,
    val page: String = "1",
    val size: String = "30",
)