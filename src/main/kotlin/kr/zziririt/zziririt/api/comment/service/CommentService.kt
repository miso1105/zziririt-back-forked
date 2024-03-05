package kr.zziririt.zziririt.api.comment.service

import kr.zziririt.zziririt.api.comment.dto.CommentDto
import kr.zziririt.zziririt.domain.comment.repository.CommentRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.post.repository.PostRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val postRepository: PostRepository,
    private val socialMemberRepository: SocialMemberRepository,
    private val commentRepository: CommentRepository
) {
    fun createComment(postId: Long, commentDto: CommentDto, userPrincipal: UserPrincipal) {
        val findSocialMember =
            socialMemberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(
                ErrorCode.MODEL_NOT_FOUND
            )
        val findPost = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val saveComment = commentDto.to(findSocialMember, findPost)

        commentRepository.save(saveComment)
    }

    @Transactional
    fun updateComment(postId: Long, commentId: Long, commentDto: CommentDto, userPrincipal: UserPrincipal) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(
            ErrorCode.MODEL_NOT_FOUND
        )
        postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findComment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        if (commentDto.privateStatus != findComment.privateStatus) {
            findComment.togglePrivateStatus()
        }
        findComment.update(content = commentDto.content)
    }

    fun deleteComment(commentId: Long, userPrincipal: UserPrincipal) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findComment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        commentRepository.delete(findComment)
    }

    @Transactional
    fun incrementZzirit(commentId: Long) {
        val findComment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        findComment.incrementZzirit()
    }
}
