package kr.zziririt.zziririt.infra.querydsl.post

import com.querydsl.core.types.dsl.BooleanExpression
import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.domain.board.model.QBoardEntity
import kr.zziririt.zziririt.domain.member.model.QSocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.QPostEntity
import kr.zziririt.zziririt.infra.querydsl.QueryDslSupport
import kr.zziririt.zziririt.infra.querydsl.post.dto.PostRowDto
import kr.zziririt.zziririt.infra.querydsl.post.dto.QPostRowDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils

@Repository
class PostQueryDslRepositoryImpl : PostQueryDslRepository, QueryDslSupport() {
    private val post = QPostEntity.postEntity
    private val socialMember = QSocialMemberEntity.socialMemberEntity
    private val board = QBoardEntity.boardEntity
    override fun searchByWhere(condition: PostSearchCondition, pageable: Pageable): PageImpl<PostRowDto> {
        val content = queryFactory
            .select(
                QPostRowDto(
                    post.id.`as`("postId"),
                    post.title,
                    post.socialMember.id.`as`("memberId"),
                    post.socialMember.nickname,
                    post.hit,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .leftJoin(post.board, board)
            .where(
                when (condition.searchType) {
                    "TITLECONT" -> {
                        titleLike(condition.searchTerm)
                        contentLike(condition.searchTerm)
                    }

                    "NICKNAME" -> {
                        nicknameLike(condition.searchTerm)
                    }

                    else -> null
                }
            ).orderBy(post.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch().toList()

        val count: Long = queryFactory
            .select(post.count())
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .where(
                when (condition.searchType) {
                    "TITLECONT" -> {
                        titleLike(condition.searchTerm)
                        contentLike(condition.searchTerm)
                    }

                    "NICKNAME" -> {
                        nicknameLike(condition.searchTerm)
                    }

                    else -> null
                }
            )
            .offset(pageable.offset * (pageable.pageNumber - 1L))
            .limit(pageable.pageSize.toLong())
            .fetchOne() ?: 0

        return PageImpl(content, pageable, count)
    }

    private fun titleLike(title: String?): BooleanExpression? {
        return if (StringUtils.hasText(title)) post.title.containsIgnoreCase(title) else null
    }

    private fun contentLike(content: String?): BooleanExpression? {
        return if (StringUtils.hasText(content)) post.content.containsIgnoreCase(content) else null
    }

    private fun nicknameLike(nickname: String?): BooleanExpression? {
        return if (StringUtils.hasText(nickname)) post.socialMember.nickname.containsIgnoreCase(nickname) else null
    }
}