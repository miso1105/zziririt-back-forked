package kr.zziririt.zziririt.infra.querydsl.post

import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.infra.querydsl.post.dto.PostRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface PostQueryDslRepository {
    fun findAll(pageable: Pageable): PageImpl<PostRowDto>
    fun findAllByBoardId(boardId: Long, pageable: Pageable): PageImpl<PostRowDto>
    fun searchByWhere(condition: PostSearchCondition, pageable: Pageable): PageImpl<PostRowDto>
}