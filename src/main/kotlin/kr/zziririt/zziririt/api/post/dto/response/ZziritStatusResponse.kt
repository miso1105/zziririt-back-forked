package kr.zziririt.zziririt.api.post.dto.response

import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntity
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntityType

data class ZziritStatusResponse(
    val zziritToEntityId: Long,
    val zziritEntityType: ZziritEntityType,
    val zziritFromMemberId: Long,
    val isZzirit: Boolean
) {
    companion object {
        fun of(zziritEntity: ZziritEntity) = ZziritStatusResponse(
            zziritToEntityId = zziritEntity.entityId,
            zziritEntityType = zziritEntity.zziritEntityType,
            zziritFromMemberId = zziritEntity.socialMember.id!!,
            isZzirit = zziritEntity.isZzirit
        )
    }
}