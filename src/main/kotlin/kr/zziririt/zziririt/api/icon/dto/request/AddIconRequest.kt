package kr.zziririt.zziririt.api.icon.dto.request

import kr.zziririt.zziririt.domain.icon.model.IconEntity

data class AddIconRequest(
    val iconName: String,
    val iconApplicationUrl: String,
    val iconMaker: String,
) {
    fun toEntity(iconUrl: String): IconEntity = IconEntity(
        iconName = iconName,
        iconUrl = iconUrl,
        iconApplicationUrl = iconApplicationUrl,
        iconMaker = iconMaker
    )
}