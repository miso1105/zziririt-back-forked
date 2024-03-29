package kr.zziririt.zziririt.infra.jpa.zzirit

import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntity
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntityType
import org.springframework.data.jpa.repository.JpaRepository

interface ZziritJpaRepository: JpaRepository<ZziritEntity, Long> {
    fun findBySocialMemberIdAndEntityIdAndZziritEntityType(socialMemberId: Long, entityId: Long, entityType: ZziritEntityType): ZziritEntity?
    fun countByIsZziritAndEntityIdAndZziritEntityTypeAndIsDeleted(isZzirit: Boolean, entityId: Long, entityType: ZziritEntityType, isDeleted: Boolean): Long
}
