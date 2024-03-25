package kr.zziririt.zziririt.infra.jpa.iconproduct

import kr.zziririt.zziririt.domain.iconproduct.model.IconProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface IconProductJpaRepository : JpaRepository<IconProductEntity, Long> {
}