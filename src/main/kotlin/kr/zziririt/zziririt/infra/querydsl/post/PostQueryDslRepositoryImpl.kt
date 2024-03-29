package kr.zziririt.zziririt.infra.querydsl.post

import com.querydsl.core.types.dsl.BooleanExpression
import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import kr.zziririt.zziririt.domain.board.model.QBoardEntity
import kr.zziririt.zziririt.domain.board.model.QCategoryEntity
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
    private val category = QCategoryEntity.categoryEntity
    override fun findAll(pageable: Pageable): PageImpl<PostRowDto> {
        val content = queryFactory
            .select(
                QPostRowDto(
                    post.id.`as`("postId"),
                    post.zziritCount,
                    post.board.boardName,
                    post.category.id,
                    post.category.categoryName,
                    post.title,
                    post.socialMember.id.`as`("memberId"),
                    post.socialMember.nickname,
                    post.privateStatus,
                    post.hit,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .leftJoin(post.board, board)
            .leftJoin(post.category, category)
            .where(post.privateStatus.eq(false))
            .orderBy(post.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch().toList()

        val count: Long = queryFactory
            .select(post.count())
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .where(post.privateStatus.eq(false))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetchOne() ?: 0

        return PageImpl(content, pageable, count)
    }

    override fun findAllByBoardId(boardId: Long, pageable: Pageable): PageImpl<PostRowDto> {
        val content = queryFactory
            .select(
                QPostRowDto(
                    post.id.`as`("postId"),
                    post.zziritCount,
                    post.board.boardName,
                    post.category.id,
                    post.category.categoryName,
                    post.title,
                    post.socialMember.id.`as`("memberId"),
                    post.socialMember.nickname,
                    post.privateStatus,
                    post.hit,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .leftJoin(post.board, board)
            .leftJoin(post.category, category)
            .where(post.board.id.eq(boardId))
            .orderBy(post.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch().toList()

        val count: Long = queryFactory
            .select(post.count())
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .where(post.board.id.eq(boardId))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetchOne() ?: 0

        return PageImpl(content, pageable, count)
    }

    override fun searchByWhere(
        boardId: Long,
        condition: PostSearchCondition,
        pageable: Pageable
    ): PageImpl<PostRowDto> {
        val content = queryFactory
            .select(
                QPostRowDto(
                    post.id.`as`("postId"),
                    post.zziritCount,
                    post.board.boardName,
                    post.category.id,
                    post.category.categoryName,
                    post.title,
                    post.socialMember.id.`as`("memberId"),
                    post.socialMember.nickname,
                    post.privateStatus,
                    post.hit,
                    post.createdAt
                )
            )
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .leftJoin(post.board, board)
            .leftJoin(post.category, category)
            .where(
                when (condition.searchType) {
                    "TITLECONT" -> {
                        titleLikeOrContentLike(condition.searchTerm)
                    }

                    "NICKNAME" -> {
                        nicknameLike(condition.searchTerm)
                    }

                    else -> null
                }
            ).where(
                post.privateStatus.eq(false)
            ).where(
                boardIdEq(boardId)
            ).where(
                categoryIdEq(condition.categoryId)
            )
            .orderBy(post.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch().toList()

        val count: Long = queryFactory
            .select(post.count())
            .from(post)
            .leftJoin(post.socialMember, socialMember)
            .leftJoin(post.category, category)
            .where(
                when (condition.searchType) {
                    "TITLECONT" -> {
                        titleLikeOrContentLike(condition.searchTerm)
                    }

                    "NICKNAME" -> {
                        nicknameLike(condition.searchTerm)
                    }

                    else -> null
                }
            ).where(
                post.privateStatus.eq(false)
            ).where(
                boardIdEq(boardId)
            ).where(
                categoryIdEq(condition.categoryId)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetchOne() ?: 0

        return PageImpl(content, pageable, count)
    }

    private fun boardIdEq(boardId: Long): BooleanExpression? {
        if (boardId == 0L) return null
        return post.board.id.eq(boardId)
    }

    private fun categoryIdEq(categoryId: Long?): BooleanExpression? {
        return categoryId?.let { post.category.id.eq(categoryId) }
    }

    private fun titleLikeOrContentLike(searchTerm: String?): BooleanExpression? {
        return if (StringUtils.hasText(searchTerm)) post.title.containsIgnoreCase(searchTerm)
            .or(post.content.containsIgnoreCase(searchTerm)) else null
    }

    private fun nicknameLike(nickname: String?): BooleanExpression? {
        return if (StringUtils.hasText(nickname)) post.socialMember.nickname.containsIgnoreCase(nickname) else null
    }
}