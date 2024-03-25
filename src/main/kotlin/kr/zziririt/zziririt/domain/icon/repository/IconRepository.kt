package kr.zziririt.zziririt.domain.icon.repository

import kr.zziririt.zziririt.domain.icon.model.IconEntity
import org.springframework.data.repository.CrudRepository

interface IconRepository : CrudRepository<IconEntity, Long> {}