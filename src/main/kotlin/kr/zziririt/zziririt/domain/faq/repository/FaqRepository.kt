package kr.zziririt.zziririt.domain.faq.repository

import kr.zziririt.zziririt.domain.faq.model.FaqEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FaqRepository: JpaRepository<FaqEntity, Long> {

}