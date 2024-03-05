package kr.zziririt.zziririt.infra.querydsl.post

import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.infra.querydsl.post.dto.PostRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface PostQueryDslRepository {
    fun searchByWhere(condition: PostSearchCondition, pageable: Pageable): PageImpl<PostRowDto>
}