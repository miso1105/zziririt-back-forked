package kr.zziririt.zziririt.infra.querydsl.zzirit

import kr.zziririt.zziririt.infra.querydsl.zzirit.dto.ZziritDto
import java.time.LocalDateTime

interface ZziritQueryDslRepository {

    fun findZziritRankInPosts(range: LocalDateTime): List<ZziritDto>

}