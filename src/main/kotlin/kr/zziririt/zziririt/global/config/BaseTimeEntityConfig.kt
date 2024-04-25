package kr.zziririt.zziririt.global.config

import kr.zziririt.zziririt.global.entity.BaseTimeEntity
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component

@Component
@EnableJpaAuditing
class BaseTimeEntityConfig: BaseTimeEntity() {
}